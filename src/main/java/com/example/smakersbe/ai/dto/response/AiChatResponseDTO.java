package com.example.smakersbe.ai.dto.response;

import com.example.smakersbe.ai.entity.AiChat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiChatResponseDTO {
    private Long aiChatId;
    private String question;
    private String answer;
    private Boolean isImportant;
    private LocalDateTime createdAt;
    private Long assetId;

    public static AiChatResponseDTO from(AiChat aiChat) {
        return AiChatResponseDTO.builder()
                .aiChatId(aiChat.getAiChatId())
                .question(aiChat.getQuestion())
                .answer(aiChat.getAnswer())
                .isImportant(aiChat.getIsImportant())
                .createdAt(aiChat.getCreatedAt())
                .assetId(aiChat.getAsset().getAssetId())
                .build();
    }


}
