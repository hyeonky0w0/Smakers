package com.example.smakersbe.ai.service;

import com.example.smakersbe.ai.dto.request.AiChatRequestDTO;
import com.example.smakersbe.ai.dto.response.AiChatResponseDTO;
import com.example.smakersbe.user.entity.User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface AiChatService {
    // 1. 질문 생성
    void sendQuestionStream(User user, AiChatRequestDTO requestDTO, Long assetId,SseEmitter emitter);

    // 2. 회원의 에셋 질문 조회
    List<AiChatResponseDTO> getChats(Long assetId, Long userId);

    // 3. 회원의 isImportant 상태 갱신
    AiChatResponseDTO updateImportantStatus(Long userId, Long aiChatId, boolean isImportant);

    void deleteChat(Long userId, Long aiChatId);

    void deleteAllChatsByAsset(Long userId, Long assetId);




}
