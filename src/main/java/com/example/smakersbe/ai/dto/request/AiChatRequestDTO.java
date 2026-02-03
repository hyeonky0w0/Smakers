package com.example.smakersbe.ai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiChatRequestDTO {

    private Long assetId;
    private String question;
    private String uuid;
}
