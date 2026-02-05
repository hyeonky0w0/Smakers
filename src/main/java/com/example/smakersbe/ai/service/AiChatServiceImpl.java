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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final AiChatRepository aiChatRepository;
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final AssetPartRepository assetPartRepository;

    // ai
    private final AiGenerateService aiGenerateService;

    // 질문 - 답변 서비스
    @Transactional
    public AiChatResponseDTO sendQuestion(AiChatRequestDTO requestDTO, Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 에셋"));

        User user = userRepository.findByUuid(requestDTO.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원"));

        // AI에게 전달하는 에셋+부품 내용 (맥락)
        String context = aiGenerateService.buildAssetContext(asset);
        String question = requestDTO.getQuestion();
        String aiAnswer = aiGenerateService.getAiAnswer(
                "너는 기계공학 학생 도우미.\n" +
                        "1. [요약], [설명] 두 섹션으로 구성할 것.\n" +
                        "2. [요약]은 1줄 이내 단답형으로 작성.\n" +
                        "3. [설명]은 3~5개의 불렛 포인트로 핵심만 나열.\n",
                "이게 내가 분석할 에셋 정보야:\n",
                2000,
                context,
                question);

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


    // 2. 회원의 에셋 질문 조회
    public List<AiChatResponseDTO> getChats(Long assetId, String uuid){

        List<AiChat> aiChats = aiChatRepository.findAllByAsset_AssetIdAndUser_UuidOrderByCreatedAtDesc(assetId, uuid);

        return aiChats.stream()
                .map(AiChatResponseDTO::from)
                .collect(Collectors.toUnmodifiableList());
    }



}
