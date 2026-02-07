package com.example.smakersbe.ai.controller;

import com.example.smakersbe.ai.dto.request.AiChatRequestDTO;
import com.example.smakersbe.ai.dto.response.AiChatResponseDTO;
import com.example.smakersbe.ai.service.AiChatService;
import com.example.smakersbe.user.entity.User;
import com.example.smakersbe.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai/assets")
@RequiredArgsConstructor
@Slf4j
public class AiChatController {

    private final AiChatService aiChatService;
    private final UserRepository userRepository;


    // 1. 질문 요청 및 응답 컨트롤러
    @PostMapping("/{assetId}/chats")
    public ResponseEntity<AiChatResponseDTO> createQuestion(
            @PathVariable Long assetId,
            @RequestBody AiChatRequestDTO requestDTO){

        log.info("AI 채팅 요청 수신: uuid={}, assetId={}, question={}",
                requestDTO.getUuid(),assetId, requestDTO.getQuestion());

        AiChatResponseDTO response = aiChatService.sendQuestion(requestDTO, assetId);
        return ResponseEntity.ok(response);
    }

    // 2. 특정 에셋에 대한 사용자 대화 이력 조회
    @GetMapping("/{assetId}/chats")
    public ResponseEntity<List<AiChatResponseDTO>> chats(
            @PathVariable Long assetId,
            @RequestParam String uuid
    ){
        List<AiChatResponseDTO> response = aiChatService.getChats(assetId, uuid);
        return ResponseEntity.ok(response);
    }

    // 3. isImportant 상태 바꾸기
    @PatchMapping("/{aiChatId}/update")
    public ResponseEntity<AiChatResponseDTO> chatIsImportant(
            @RequestHeader("X-USER-UUID") String uuid,
            @PathVariable Long aiChatId,
            @RequestParam boolean isImportant
    ){
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("해당 UUID를 가진 유저가 없습니다: " + uuid));
        Long userId = user.getUserId();

        AiChatResponseDTO response = aiChatService.updateImportantStatus(userId, aiChatId, isImportant);
        return ResponseEntity.ok(response);
    }



}
