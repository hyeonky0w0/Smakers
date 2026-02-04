package com.example.smakersbe.ai.service;

import com.example.smakersbe.ai.dto.request.AiChatRequestDTO;
import com.example.smakersbe.ai.dto.response.AiChatResponseDTO;

public interface AiChatService {
    AiChatResponseDTO sendQuestion(AiChatRequestDTO requestDTO, Long assetId);

}
