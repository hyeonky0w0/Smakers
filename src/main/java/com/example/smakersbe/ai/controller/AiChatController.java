package com.example.smakersbe.ai.controller;

import com.example.smakersbe.ai.dto.request.AiChatRequestDTO;
import com.example.smakersbe.ai.dto.response.AiChatResponseDTO;
import com.example.smakersbe.ai.service.AiChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/")
@RequiredArgsConstructor
@Slf4j
public class AiChatController {

    private final AiChatService aiChatService;


    // 1. 잘문 요청 및 응답 컨트롤러
    @PostMapping("/chat")
    public ResponseEntity<AiChatResponseDTO> sendQuestion(@RequestBody AiChatRequestDTO requestDTO){

        log.info("AI 채팅 요청 수신: uuid={}, assetId={}, question={}",
                requestDTO.getUuid(), requestDTO.getAssetId(), requestDTO.getQuestion());
        return ResponseEntity.ok(aiChatService.sendQuestion(requestDTO));
    }


}
