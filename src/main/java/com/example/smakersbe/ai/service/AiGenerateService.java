package com.example.smakersbe.ai.service;

import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.entity.Part;
import com.example.smakersbe.asset.repository.AssetPartRepository;
import com.example.smakersbe.quiz.entity.QuizSetItem;
import com.example.smakersbe.quiz.entity.QuizUserAnswer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiGenerateService {

    @Value("${openai.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate;
    private final AssetPartRepository assetPartRepository;
    private final WebClient webClient = WebClient.builder().build();



    // ai 채팅 생성
    public Flux<String> callAiStream(String systemPrompt, String userPrompt, Integer max_tokens, String context, String question) {
        String url = "https://api.openai.com/v1/chat/completions";
        String finalContent = userPrompt + context + (question != null ? "\n\n질문: " + question : "");

        // 1. 요청 바디 구성 (stream: true 설정!)
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-5-mini"); // 채연 님이 쓰시던 모델 그대로!
        requestBody.put("max_completion_tokens", max_tokens);
        requestBody.put("stream", true); // 스트리밍 활성화

        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", systemPrompt));
        messages.put(new JSONObject().put("role", "user").put("content", finalContent));
        requestBody.put("messages", messages);

        return webClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody.toString())
                .retrieve()
                .bodyToFlux(String.class) // 조각(Chunk) 단위로 받기
                .filter(data -> !data.contains("[DONE]")) // 끝 신호 제외
                .map(data -> {
                    try {
                        // 중요: OpenAI 스트리밍은 "data: " 로 시작하므로 이 글자를 지워줘야 JSONObject가 인식해요!
                        String jsonStr = data.replace("data: ", "").trim();
                        if (jsonStr.isEmpty()) return "";

                        JSONObject json = new JSONObject(jsonStr);
                        return json.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("delta")
                                .optString("content", "");
                    } catch (Exception e) {
                        return ""; // 파싱 실패 시 빈 문자열
                    }
                });
    }


    // ai 퀴즈 생성
    public String callAi(String systemPrompt, String userPrompt, Integer max_tokens, String context, String question){

        String url = "https://api.openai.com/v1/chat/completions";

        String finalContent = userPrompt + context;
        if (question != null) {
            finalContent += "\n\n질문: " + question;
        }
        try {
            // 1. 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            //cf) 종료 시간 기록 및 계산 (성능 개선)
            long startTime = System.currentTimeMillis();

            // 2. 바디 구성 (JSONObject 활용)
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "gpt-5-mini");
            requestBody.put("max_completion_tokens", max_tokens);

            JSONArray messages = new JSONArray();
            // System 메시지: 에셋 지식 주입
            messages.put(new JSONObject().put("role", "system").put("content", systemPrompt));

            // User 메시지: 실제 질문
            messages.put(new JSONObject().put("role", "user").put("content", finalContent));
            requestBody.put("messages", messages);


            // 3. API 호출
            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
            String response = restTemplate.postForObject(url, entity, String.class);

            // 생성 시간 측정
            long endTime = System.currentTimeMillis();
            double duration = (endTime - startTime) / 1000.0;

            log.info("AI 답변 생성 소요 시간: {}초", duration);
            log.info("AI 응답 원본\n: {}", response);

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



    // 질문용 ai 생성기
//    public String getAiAnswer(String systemPrompt, String userPrompt, Integer max_tokens, String context, String question) {
//        return callAi(systemPrompt, userPrompt, max_tokens, context, question);
//    }
    // 퀴즈용 ai 생성기
    public String getAiAnswer(String systemPrompt, String userPrompt, Integer max_tokens, String context) {
        return callAi(systemPrompt, userPrompt, max_tokens, context, null);
    }


    // 1. 에셋에 대한 context 생성하는 함수
    public String buildAssetContext(Asset asset) {
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

    // 2. 에셋에 대한 context + 메모 정보로 전체 context 만들기
    public String buildAssetAndMemoContext(Asset asset, String userNotes) {
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
        sb.append("\n### 사용자의 학습 메모 ###\n");

        if (userNotes == null || userNotes.isBlank()) {
            sb.append("- 추가된 메모가 없습니다. 에셋 정보를 바탕으로 퀴즈를 생성하세요.\n");
        } else {
            sb.append(userNotes).append("\n");
        }

        return sb.toString();
    }

    // 퀴즈 생성을 위한 문맥 생성 함수 (오버로딩)
    public String buildAssetContext(List<QuizUserAnswer> wrongAnswers) {
        if (wrongAnswers == null || wrongAnswers.isEmpty()) {
            return "모든 문제를 맞히셨습니다! 분석할 오답이 없습니다.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("### 사용자의 오답 분석 데이터 ###\n");

        // 첫 번째 데이터에서 에셋 정보를 가져와서 상단에 표시 (옵션)
        Asset asset = wrongAnswers.get(0).getQuizSetItem().getQuizSet().getAsset();
        sb.append("대상 에셋: ").append(asset.getAssetName()).append("\n\n");

        for (int i = 0; i < wrongAnswers.size(); i++) {
            QuizUserAnswer ua = wrongAnswers.get(i);
            QuizSetItem item = ua.getQuizSetItem();

            sb.append(i + 1).append(". 틀린 문제 정보\n");
            sb.append("   - 질문: ").append(item.getQuestion()).append("\n");
            sb.append("   - 정답 번호: ").append(item.getAnswer()).append("\n");
            sb.append("   - 사용자가 선택한 번호: ").append(ua.getUserChoice()).append("\n");
            sb.append("   - 정답 해설: ").append(item.getExplanation()).append("\n\n");
        }

        return sb.toString();
    }



}