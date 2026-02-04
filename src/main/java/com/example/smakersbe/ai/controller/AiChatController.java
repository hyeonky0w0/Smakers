package com.example.smakersbe.ai.controller;

import com.example.smakersbe.ai.dto.request.AiChatRequestDTO;
import com.example.smakersbe.ai.dto.response.AiChatResponseDTO;
import com.example.smakersbe.ai.service.AiChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai/")
@RequiredArgsConstructor
@Slf4j
public class AiChatController {

    private final AiChatService aiChatService;


    // 1. 잘문 요청 및 응답 컨트롤러
    @PostMapping("/assets/{assetId}/chats")
    public ResponseEntity<AiChatResponseDTO> createQuestion(
            @PathVariable Long assetId,
            @RequestBody AiChatRequestDTO requestDTO){

        log.info("AI 채팅 요청 수신: uuid={}, assetId={}, question={}",
                requestDTO.getUuid(),assetId, requestDTO.getQuestion());

        AiChatResponseDTO response = aiChatService.sendQuestion(requestDTO, assetId);
        return ResponseEntity.ok(response);
    }

    // 2. 특정 에셋에 대한 사용자 대화 이력 조회
    @GetMapping("/assets/{assetId}/chats")
    public ResponseEntity<List<AiChatResponseDTO>> chats(
            @PathVariable Long assetId,
            @RequestParam String uuid
    ){
        List<AiChatResponseDTO> response = aiChatService.getChats(assetId, uuid);
        return ResponseEntity.ok(response);
    }



}
