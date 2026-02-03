package com.example.smakersbe.ai.service;

import com.example.smakersbe.ai.dto.request.AiChatRequestDTO;
import com.example.smakersbe.ai.dto.response.AiChatResponseDTO;
import com.example.smakersbe.ai.entity.AiChat;
import com.example.smakersbe.ai.repository.AiChatRepository;
import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.repository.AssetPartRepository;
import com.example.smakersbe.asset.repository.AssetRepository;
import com.example.smakersbe.user.entity.User;
import com.example.smakersbe.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import com.example.smakersbe.asset.entity.Part;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final AiChatRepository aiChatRepository;
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final AssetPartRepository assetPartRepository;

    // ai
    @Value("${openai.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate;

    // 질문 - 답변 서비스
    @Transactional
    public AiChatResponseDTO sendQuestion(AiChatRequestDTO requestDTO) {
        Asset asset = assetRepository.findById(requestDTO.getAssetId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 에셋"));

        User user = userRepository.findByUuid(requestDTO.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원"));

        // AI에게 전달하는 에셋+부품 내용 (맥락)
        String context= buildAssetContext(asset);
        String aiAnswer = getAiAnswer(requestDTO.getQuestion(), context);

        // ai 호출 메서드
        AiChat aiChat = AiChat.builder()
                .question(requestDTO.getQuestion())
                .answer(aiAnswer)
                .user(user)
                .asset(asset)
                .createdAt(LocalDateTime.now())
                .isImportant(false)
                .build();

        log.info("성공적으로 받아온 답변: {}", aiAnswer);

        aiChatRepository.save(aiChat);
        return AiChatResponseDTO.from(aiChat);

    }

    // 1. 에셋에 대한 context 생성하는 함수
    private String buildAssetContext(Asset asset) {
        StringBuilder sb = new StringBuilder();
        sb.append("### 에셋 정보 ###\n");
        sb.append("명칭: ").append(asset.getAssetName()).append("\n");
        sb.append("설명: ").append(asset.getAssetDescription()).append("\n\n");

        sb.append("### 포함된 부품 리스트 및 상세 설명 ###\n");

        // 에셋에 대한 부품들 가져오기
        List<Part> parts = assetPartRepository.findAllByAsset(asset);
        if (parts.isEmpty()) {
            sb.append("- 등록된 부품 정보가 없습니다.\n");
        } else {
            parts.forEach(part -> {
                sb.append("-부품명 ").append(part.getPartName())
                        .append(" (상세 설명: ").append(part.getPartDescription()).append(")\n");
            });
        }
        return sb.toString();
    }

    // 2. ai 호출 메서드
    private String getAiAnswer(String question, String context) {


        String url = "https://api.openai.com/v1/chat/completions";
        try {
            // 1. 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey); // @Value("${openai.api.key}")로 받은 키

            //cf) 종료 시간 기록 및 계산 (성능 개선)
            long startTime = System.currentTimeMillis();

            // 2. 바디 구성 (JSONObject 활용)
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "gpt-5-mini");
            requestBody.put("max_completion_tokens", 2000);

            JSONArray messages = new JSONArray();

            // System 메시지: 에셋 지식 주입
            messages.put(new JSONObject()
                    .put("role", "system")
                    .put("content", "너는 기계공학 학생 도우미.\n" +
                            "1. [요약], [설명] 두 섹션으로 구성할 것.\n" +
                            "2. [요약]은 1줄 이내 단답형으로 작성.\n" +
                            "3. [설명]은 3~5개의 불렛 포인트로 핵심만 나열.\n"));

            // User 메시지: 실제 질문
            messages.put(new JSONObject()
                    .put("role", "user")
                    .put("content", "이게 내가 분석할 에셋 정보야:\n"+ context + "\n\n질문: " + question));

            requestBody.put("messages", messages);

            // 3. API 호출
            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
            String response = restTemplate.postForObject(url, entity, String.class);

            // 생성 시간 측정
            long endTime = System.currentTimeMillis();
            double duration = (endTime - startTime) / 1000.0;

            log.info("AI 답변 생성 소요 시간: {}초", duration);
            log.info("AI 응답 원본: {}", response);

            // 4. 응답 파싱 (JSONObject 활용)
            if (response != null) {
                JSONObject responseJson = new JSONObject(response);
                return responseJson.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
            }

        } catch (Exception e) {
            log.error("AI 호출 중 에러 발생: ", e);
            return "현재 분석 요청이 많아 답변이 조금 늦어지고 있습니다. 잠시 후 다시 '질문하기'를 눌러주세요!";
        }

        return "답변을 가져오지 못했습니다.";



    }


}
