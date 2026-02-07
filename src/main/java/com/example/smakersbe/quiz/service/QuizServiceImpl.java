package com.example.smakersbe.quiz.service;

import com.example.smakersbe.ai.service.AiGenerateService;
import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.entity.Memo;
import com.example.smakersbe.asset.entity.UserAsset;
import com.example.smakersbe.asset.repository.AssetRepository;
import com.example.smakersbe.asset.repository.UserAssetRepository;
import com.example.smakersbe.quiz.dto.request.QuizAiAnalysisRequestDTO;
import com.example.smakersbe.quiz.dto.request.QuizAttemptRequestDTO;
import com.example.smakersbe.quiz.dto.request.QuizCreateByAiRequestDTO;
import com.example.smakersbe.quiz.dto.request.QuizCreateRequestDTO;
import com.example.smakersbe.quiz.dto.response.*;
import com.example.smakersbe.quiz.entity.*;
import com.example.smakersbe.quiz.repository.*;
import com.example.smakersbe.user.entity.User;
import com.example.smakersbe.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import com.example.smakersbe.asset.repository.MemoRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizSetRepository quizSetRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AiGenerateService aiGenerateService;
    private final AssetRepository assetRepository;
    private final QuizSetItemRepository quizSetItemRepository;
    private final UserRepository userRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final MemoRepository memoRepository;
    private final QuizUserAnswerRepository quizUserAnswerRepository;
    private final QuizResultRepository quizResultRepository;
    private final UserAssetRepository userAssetRepository;

    // 1. 퀴즈 생성 요청 서비스
    /* 사용자가 만약 풀었던 시험를 다시 풀고싶다면 -> quiz_set_id 값 함께 전달
        처음 시행하는 시험(퀴즈 세트)라면 -> quiz_set_id 값에 Null 전달
     */
    /* 퀴즈 생성 요청이 들어오면 -> 퀴즈 생성 중인 컬럼을 true로 변경 */
    @Transactional
    public List<QuizCreateResponseDTO> createQuiz(QuizCreateRequestDTO requestDTO, User user){

        Asset asset = assetRepository.findById(requestDTO.getAssetId()).orElseThrow();

        // 에셋의 메모들 모으기 (최신 3개 합침)
        List<Memo> memos = memoRepository.findTop3ByAssetOrderByCreatedAtDesc(asset);
        String memoContext = memos.stream()
                .map(Memo::getMemoContents)
                .collect(Collectors.joining(" / "));
        // 메모가 아예 없으면 기본값 설정
        if (memoContext.isEmpty()) memoContext = "기계공학 기초 개념";

        if (requestDTO.getQuizSetId() != null){
            // 시험지 id가 있으면
            Long quizSetId = requestDTO.getQuizSetId();
            // 시험지 정보 반환하기
            List<QuizSetItem> items = quizSetItemRepository.findAllByQuizSetId(quizSetId);

            return items.stream().map(item -> QuizCreateResponseDTO.builder()
                            .quizSetId(quizSetId)
                            .assetId(item.getQuizSet().getAsset().getAssetId()) // 부모 에셋 id
                            .quizSetItemId(item.getQuizSetItemId())
                            .question(item.getQuestion())
                            .options(parseOptions(item.getOptions())) // JSON String을 List로 변환
                            .hint(item.getHint())
                            .build())
                    .collect(Collectors.toList());

        } else{
            // 안 푼 시험지 반환하기
            List<Long> solvedQuizSetIds = quizAttemptRepository.findQuizSetIdsByUserAndAsset(user, asset);

            if (solvedQuizSetIds.isEmpty()) {
                solvedQuizSetIds = List.of(-1L);
            }

            // 다음에 풀 시험지 -> 가장 과거에 생성된 시험지
            final String finalMemoContext = memoContext;
            QuizSet nextQuiz = quizSetRepository.findFirstByAssetAndQuizSetIdNotInOrderByQuizSetIdAsc(asset, solvedQuizSetIds)
                    .orElseGet(() -> {
                        // 만약 DB에 아예 퀴즈가 하나도 없다면 동기로 하나 생성
                        return createTestByAi(new QuizCreateByAiRequestDTO(asset.getAssetId(), finalMemoContext));
                    });

            // 풀지 않은 시험지가 2개 이하면 -> 비동기로 퀴즈 생성 요청
            long remainingCount = quizSetRepository.countByAssetAndQuizSetIdNotIn(asset, solvedQuizSetIds);
            if (remainingCount <= 2 && !asset.isQuizCreating()) {
                log.info("에셋 ID {}의 재고 부족! 남은 개수: {}", asset.getAssetId(),remainingCount);
                // 퀴즈 생성 상태 바꿔주기
                asset.updateQuizCreatingStatus(true);
                assetRepository.save(asset);
                // 비동기 호출
                CompletableFuture.runAsync(() -> {
                    try {
                        createTestByAi(new QuizCreateByAiRequestDTO(asset.getAssetId(), finalMemoContext));
                    } finally {
                        // 퀴즈 생성하고 나면  false 로 바꿔주기
                        Asset targetAsset = assetRepository.findById(asset.getAssetId()).orElseThrow();
                        targetAsset.updateQuizCreatingStatus(false);
                        assetRepository.save(targetAsset);
                    }
                });
            }
            return getExistingQuizItems(nextQuiz.getQuizSetId());
        }
    }

    // 2. 퀴즈 답안지 작성 및 등록 서비스
    /* 유저가 답을 제출하면 -> 그 결과를 QuizUserAnswer, QuizAttempt에 저장 -> 내부 로직에 의해 채점
    -> 그 결과가 QuizResult에 저장 (이때 aiReview는 기본값) -> 이후 ai 리뷰 보기 클릭하면 -> aiReview 값 업데이트 */
    public QuizAttemptResponseDTO createQuizAttempt(QuizAttemptRequestDTO requestDTO){

        // 유저 및 퀴즈 세트 조회
        User user = userRepository.findByUuid(requestDTO.getUuid())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        QuizSet quizSet = quizSetRepository.findById(requestDTO.getQuizSetId())
                .orElseThrow(() -> new RuntimeException("퀴즈 세트를 찾을 수 없습니다."));

        // QuizAttempt에 저장
        QuizAttempt quizAttempt = QuizAttempt.builder()
                . user(user)
                .quizSet(quizSet).build();
        quizAttemptRepository.save(quizAttempt);

        // 채점 + QuizUserAnswer 저장
        long correctCount = 0;
        List<QuizUserAnswer> userAnswers = new ArrayList<>();

        for (QuizAttemptRequestDTO.UserAnswerDTO answerDTO : requestDTO.getAnswers()){
            QuizSetItem quizSetItem = quizSetItemRepository.findById(answerDTO.getQuizSetItemId())
                    .orElseThrow(() -> new RuntimeException("문제를 찾을 수 없습니다."));

            // 채점
            boolean isCorrect = quizSetItem.getAnswer().equals(answerDTO.getUserChoice());
            if(isCorrect) correctCount++;

            // 결과 저장
            QuizUserAnswer quizUserAnswer = QuizUserAnswer.builder()
                    .userChoice(answerDTO.getUserChoice())
                    .isCorrect(isCorrect)
                    .quizAttempt(quizAttempt).
                    quizSetItem(quizSetItem).build();
            userAnswers.add(quizUserAnswer);
        }
        quizUserAnswerRepository.saveAll(userAnswers);

        // QuizResult 저장
        QuizResult quizResult = QuizResult.builder()
                .quizAttempt(quizAttempt)
                .score((correctCount * 100) / requestDTO.getAnswers().size())
                .totalCount((long)requestDTO.getAnswers().size())
                .correctCount(correctCount)
                .aiReview("AI 분석을 요청해주세요.")
                .build();
        quizResultRepository.save(quizResult);

        // response
        return QuizAttemptResponseDTO.from(quizAttempt, userAnswers, quizResult);
    }

    // 3. quiz 에 대한 ai 분석 요청
    /* 사용자의 quizAttemptId 데이터를 전송 -> QuizUserAnswer에서 quizAttemptId와 일치하고, isCorrect가 false인 데이터 전송 (quizSetItem과 조인)
    -> Ai 문맥 만들어서 -> AI의 분석 제공
     */
    public QuizAiAnalysisResponseDTO createQuizAiAnalyze(QuizAiAnalysisRequestDTO requestDTO){

        QuizAttempt attempt = quizAttemptRepository.findById(requestDTO.getQuizAttemptId())
                .orElseThrow(() -> new EntityNotFoundException("퀴즈 시도 이력을 찾을 수 없습니다."));

        if (!attempt.getUser().getUuid().equals(requestDTO.getUuid())) {
            throw new IllegalArgumentException("해당 퀴즈 기록에 접근할 권한이 없습니다.");
        }

        // 유저가 틀린 문제 + 답안 가져오기
        List<QuizUserAnswer> wrongAnswers = quizUserAnswerRepository
                .findWrongAnswersWithItemByAttemptId(requestDTO.getQuizAttemptId());

        // 문맥 만들기
        String context = aiGenerateService.buildAssetContext(wrongAnswers);

        // 프롬프트
        String getAiAnswer = aiGenerateService.getAiAnswer(
                "너는 시험 분석 및 조언 전문가.\n"+
                        "1. 말투: 친절한 선배나 멘토처럼 (~해요, ~입니다)\n"+
                        "구조: [총평], [핵심 오답 분석], [향후 학습 가이드] 세 부분으로 나눌 것.\n"+
                        "분석 포인트: 유저가 선택한 오답과 정답을 비교하여 오개념을 정확히 짚어줄 것.\n"+
                        "제한: 너무 길지 않게, 핵심 위주로 500자 이내로 작성해줘.",
                "이게 사용자가 틀린 문제 정보야.\n",
                2000,
                context);

        // 기존의 result 데이터 가져오기
        QuizResult quizResult = quizResultRepository.findByQuizAttempt(attempt)
                .orElseGet(() -> QuizResult.builder().quizAttempt(attempt).build());

        quizResult.updateAiReview(getAiAnswer);
        quizResultRepository.save(quizResult);

        log.info("성공적으로 AI 리뷰가 저장되었습니다. 받아온 답변: {}, Attempt ID: {}", getAiAnswer,attempt.getQuizAttemptId());
        return QuizAiAnalysisResponseDTO.from(quizResult);
    }

    // 4. 특정 에셋에 대한 사용자의 퀴즈 히스토리 조회
    public List<QuizHistoryResponseDTO> fetchMyQuizHistory(String uuid, Long assetId){
        List<QuizAttempt> quizAttempts = quizAttemptRepository.findAllByUuidAndAssetId(uuid, assetId);

        return quizAttempts.stream()
                .map(attempt -> {
                    QuizResult result = quizResultRepository.findByQuizAttempt(attempt).orElse(null);

                    List<QuizUserAnswer> allAnswers = quizUserAnswerRepository.findAllByQuizAttempt(attempt);
                    List<QuizUserAnswer> wrongAnswers = allAnswers.stream() //전체 데이터 중
                            .filter(answer -> !answer.getIsCorrect()) // 틀린 것만 필터링
                            .toList();

                    return QuizHistoryResponseDTO.from(attempt, result, wrongAnswers);
                })
                .toList();
    }

    // 5. 사용자가 학습했던 에셋에 대해서만 퀴즈 썸네일 반환
    // userAsset 테이블에 있는 assetId을 가지고 -> asset의 assetName, assetThumbnailUrl 반환
    public List<QuizzableAssetListResponseDTO> fetchMyQuizzableAssets(String uuid){
        List<UserAsset> assets = userAssetRepository.findAllByUserUuidFetchAsset(uuid);

        return assets.stream()
                .map(ua -> QuizzableAssetListResponseDTO.builder()
                        .assetId(String.valueOf(ua.getAsset().getAssetId()))
                        .assetName(ua.getAsset().getAssetName())
                        .assetThumbnailUrl(ua.getAsset().getAssetThumbnailUrl())
                        .build())
                .toList();
    }

    // options 파싱을 위한 메서드
    private List<String> parseOptions(String optionsJson) {
        try {
            // JSON 문자열을 List<String>으로 변환
            return objectMapper.readValue(optionsJson, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            // 파싱 실패 시
            log.error("옵션 파싱 에러: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // 에셋 + 부품 + 메모맥락
    @Transactional
    QuizSet createTestByAi(QuizCreateByAiRequestDTO requestDTO){
        // ai로 생성한 퀴즈들을 묶기

        Asset asset = assetRepository.findById(requestDTO.getAssetId())
                .orElseThrow(() -> new EntityNotFoundException("해당 에셋이 없어요!"));
        // 전체 맥락
        String context = aiGenerateService.buildAssetAndMemoContext(asset, requestDTO.getMemoContents());

        String aiQuizJson = aiGenerateService.getAiAnswer(
                "너는 기계공학 퀴즈 출제 위원이자 학습 가이드야.\n" +
                        "- 답변 톤: 전문적이면서도 친절한 '-해요'체를 사용해 주세요. (단, '정답이세요' 같은 과한 존칭은 절대 금지)\n" +
                        "- 반드시 JSON 배열 형식으로만 응답해: [{\"question\": \"...\", \"options\": [\"...\"], \"answer\": 0, \"explanation\": \"...\", \"hint\": \"...\"}]",

                "이 정보를 바탕으로 퀴즈 4개를 생성해 주세요.\n" +
                        "1. 해설(explanation) 작성 규칙:\n" +
                        "   - '정답은 ~번입니다', '정답이세요'와 같은 문구는 생략하고 곧바로 기술적인 원리를 설명하세요.\n" +
                        "   - 해당 부품이 시스템 내에서 수행하는 핵심 기능과 원리를 중심으로 설명해 주세요.\n" +
                        "   - 예: '이 부품은 회전 운동을 직선 운동으로 변환하여 이동 죠를 움직이는 핵심 역할을 수행해요.' (O)\n" +
                        "2. 힌트(hint) 작성 규칙:\n" +
                        "   - 직접적인 정답 단어를 절대 포함하지 마세요.\n" +
                        "   - 부품의 물리적 특징이나 위치를 질문하는 유도 심문을 사용해 보세요.\n" +
                        "3. 모든 문장은 문법에 맞는 표준 구어체(-해요, -입니다)를 유지해 주세요.\n" +
                        "4. 사용자의 메모 내용은 비밀스럽게 지식에 녹여내어 문제를 구성해 주세요.",
                5000,
                context);

        if (aiQuizJson == null || aiQuizJson.startsWith("현재")) {
            log.error("AI 응답이 올바르지 않습니다: {}", aiQuizJson);
            throw new RuntimeException("AI 서비스 응답 오류");
        }

        String cleanedJson = aiQuizJson.replaceAll("```json|```", "").trim();   //json 부분 추출

        // objectMapper : ai에게 받은 String 을 json으로 변환
        try {
            List<QuizCreateByAiResponseDTO> responseDTOS = objectMapper.readValue(cleanedJson, new TypeReference<List<QuizCreateByAiResponseDTO>>() {});

            // QuizSet(시험지 묶음) 생성 및 저장
            QuizSet quizSet = QuizSet.builder()
                    .asset(asset)
                    .build();
            quizSetRepository.save(quizSet);

            // 퀴즈 하나씩 꺼내서 QuizSetItem 엔티티로 변환해서 저장
            for (QuizCreateByAiResponseDTO dto : responseDTOS) {
                // 퀴즈 한 문제의 options을 String으로 바꾸기 -> objectMapper에서 다시 json으로 바꾼다.
                String jsonOptions = objectMapper.writeValueAsString(dto.getOptions());
                QuizSetItem quizItem = QuizSetItem.builder()
                        .question(dto.getQuestion())
                        .options(jsonOptions)
                        .answer(dto.getAnswer())
                        .explanation(dto.getExplanation())
                        .hint(dto.getHint())
                        .quizSet(quizSet)
                        .build();

                quizSetItemRepository.save(quizItem);
            }
            log.info("AI 퀴즈 세트 생성 완료: QuizSetId {}", quizSet.getQuizSetId());
            return quizSet;

        } catch (Exception e) {
            log.error("AI 퀴즈 생성 및 파싱 중 에러 발생: ", e);
            throw new RuntimeException("퀴즈 저장에 실패했습니다.");
        }

    }

    private List<QuizCreateResponseDTO> getExistingQuizItems(Long quizSetId) {
        // 1. quizSetId로 해당 시험지의 모든 문항(Item) 가져오기
        List<QuizSetItem> items = quizSetItemRepository.findAllByQuizSetId(quizSetId);

        return items.stream().map(item -> QuizCreateResponseDTO.builder()
                        .quizSetId(quizSetId)
                        .assetId(item.getQuizSet().getAsset().getAssetId())
                        .quizSetItemId(item.getQuizSetItemId())
                        .question(item.getQuestion())
                        .options(parseOptions(item.getOptions())) // 우리가 만든 JSON 파싱 메서드 활용!
                        .hint(item.getHint())
                        .build())
                .collect(Collectors.toList());
    }

}
