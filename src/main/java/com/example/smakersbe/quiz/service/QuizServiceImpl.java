package com.example.smakersbe.quiz.service;

import com.example.smakersbe.ai.service.AiGenerateService;
import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.entity.Memo;
import com.example.smakersbe.asset.repository.AssetRepository;
import com.example.smakersbe.quiz.dto.request.QuizCreateByAiRequestDTO;
import com.example.smakersbe.quiz.dto.request.QuizCreateRequestDTO;
import com.example.smakersbe.quiz.dto.response.QuizCreateResponseDTO;
import com.example.smakersbe.quiz.entity.QuizSet;
import com.example.smakersbe.quiz.entity.QuizSetItem;
import com.example.smakersbe.quiz.repository.QuizAttemptRepository;
import com.example.smakersbe.quiz.repository.QuizSetItemRepository;
import com.example.smakersbe.quiz.repository.QuizSetRepository;
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

    // 1. 퀴즈 생성 요청 서비스
    /* 사용자가 만약 풀었던 시험를 다시 풀고싶다면 -> quiz_set_id 값 함께 전달
        처음 시행하는 시험(퀴즈 세트)라면 -> quiz_set_id 값에 Null 전달
     */
    /* 퀴즈 생성 요청이 들어오면 -> 퀴즈 생성 중인 컬럼을 true로 변경 */
    public List<QuizCreateResponseDTO> createQuiz(QuizCreateRequestDTO requestDTO){

        User user = userRepository.findByUuid(requestDTO.getUuid()).orElseThrow();
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
            List<QuizSetItem> items = quizSetRepository.findAllByQuizSetId(quizSetId);

            return items.stream().map(item -> QuizCreateResponseDTO.builder()
                            .quizSetId(quizSetId)
                            .assetId(item.getQuizSet().getAsset().getAssetId()) // 부모 에셋 id
                            .quizSetItemId(item.getQuizSetItemId())
                            .question(item.getQuestion())
                            .options(parseOptions(item.getOptions())) // JSON String을 List로 변환
                            .answer(item.getAnswer())
                            .explanation(item.getExplanation())
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
                        return createTestByAi(new QuizCreateByAiRequestDTO(asset.getAssetId(), "..."));
                    });

            // 풀지 않은 시험지가 2개 이하면 -> 비동기로 퀴즈 생성 요청
            long remainingCount = quizSetRepository.countByAssetAndQuizSetIdNotIn(asset, solvedQuizSetIds);
            if (remainingCount <= 2) {
                log.info("재고 부족(남은 개수: {})! 비동기로 퀴즈 생성을 시작합니다.", remainingCount);
                // @Async가 붙은 메서드를 호출하거나 별도 스레드로 실행
                CompletableFuture.runAsync(() -> {
                    createTestByAi(new QuizCreateByAiRequestDTO(asset.getAssetId(), finalMemoContext));
                });
            }
            return getExistingQuizItems(nextQuiz.getQuizSetId());
        }



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
                "너는 기계공학 퀴즈 생성 도우미. 반드시 JSON 배열 형식으로만 응답해. [{\"question\": \"...\", \"options\": [\"...\"], \"answer\": 0, \"explanation\": \"...\", \"hint\": \"...\"}]",
                "이 정보를 바탕으로 퀴즈 4개를 만들어줘.\n" + "선지(options)는 각 퀴즈당 4개야 :\n" + "각 문제에 대한 힌트(hint)도 생성해줘",
                5000,
                context);
        String cleanedJson = aiQuizJson.replaceAll("```json|```", "").trim();   //json 부분 추출

        // objectMapper : ai에게 받은 String 을 json으로 변환
        try {
            List<QuizCreateResponseDTO> responseDTOS = objectMapper.readValue(cleanedJson, new TypeReference<List<QuizCreateResponseDTO>>() {});

            // QuizSet(시험지 묶음) 생성 및 저장
            QuizSet quizSet = QuizSet.builder()
                    .asset(asset)
                    .build();
            quizSetRepository.save(quizSet);

            // 퀴즈 하나씩 꺼내서 QuizSetItem 엔티티로 변환해서 저장
            for (QuizCreateResponseDTO dto : responseDTOS) {
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
        // 1. quizSetId로 해당 시험지의 모든 문항(Item)을 가져옵니다.
        List<QuizSetItem> items = quizSetItemRepository.findAllByQuizSet_QuizSetId(quizSetId);

        // 2. 엔티티 리스트를 DTO 리스트로 변환합니다.
        return items.stream().map(item -> QuizCreateResponseDTO.builder()
                        .quizSetId(quizSetId)
                        .assetId(item.getQuizSet().getAsset().getAssetId())
                        .quizSetItemId(item.getQuizSetItemId())
                        .question(item.getQuestion())
                        .options(parseOptions(item.getOptions())) // 우리가 만든 JSON 파싱 메서드 활용!
                        .answer(item.getAnswer())
                        .explanation(item.getExplanation())
                        .hint(item.getHint())
                        .build())
                .collect(Collectors.toList());
    }



}
