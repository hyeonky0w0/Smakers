package com.example.smakersbe.ai.service;

import com.example.smakersbe.ai.dto.request.AiChatRequestDTO;
import com.example.smakersbe.ai.dto.response.AiChatResponseDTO;

import java.util.List;

public interface AiChatService {
    // 1. 질문 생성
    AiChatResponseDTO sendQuestion(AiChatRequestDTO requestDTO, Long assetId);

    // 2. 회원의 에셋 질문 조회
    List<AiChatResponseDTO> getChats(Long assetId, String uuid);



}
