package com.example.smakersbe.ai.controller;

import com.example.smakersbe.ai.dto.request.AiChatRequestDTO;
import com.example.smakersbe.ai.dto.response.AiChatResponseDTO;
import com.example.smakersbe.ai.service.AiChatService;
import com.example.smakersbe.user.entity.User;
import com.example.smakersbe.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/ai/chats")
@RequiredArgsConstructor
@Slf4j
public class AiChatController {

    private final AiChatService aiChatService;
    private final UserRepository userRepository;


    // 1. 질문 요청 및 응답 컨트롤러
    @PostMapping(value = "/{assetId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter createQuestion(
            @RequestHeader("X-USER-UUID") String uuid,
            @PathVariable Long assetId,
            @RequestBody AiChatRequestDTO requestDTO) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("유저 없음"));

        // 1. 통로 생성 (타임아웃 1분 설정)
        SseEmitter emitter = new SseEmitter(60000L);

        // 2. 서비스에 emitter를 넘겨서 비동기로 답변 생성 시작!
        aiChatService.sendQuestionStream(user, requestDTO, assetId, emitter);

        // 3. 통로를 즉시 반환 (사용자는 이때부터 연결됨)
        return emitter;
    }

    // 2. 특정 에셋에 대한 사용자 대화 이력 조회
    @GetMapping("/{assetId}")
    public ResponseEntity<List<AiChatResponseDTO>> chats(
            @RequestHeader("X-USER-UUID") String uuid,
            @PathVariable Long assetId
    ){
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("해당 UUID를 가진 유저가 없습니다: " + uuid));
        Long userId = user.getUserId();


        List<AiChatResponseDTO> response = aiChatService.getChats(assetId, userId);
        return ResponseEntity.ok(response);
    }

    // 3. isImportant 상태 바꾸기
    @PatchMapping("/{aiChatId}")
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

    // 4. 특정 aiChat 삭제하기
    @DeleteMapping("/{aiChatId}")
    public ResponseEntity<Void> deleteChat(
            @RequestHeader("X-USER-UUID") String uuid,
            @PathVariable Long aiChatId) {

        User user = userRepository.findByUuid(uuid).orElseThrow();
        aiChatService.deleteChat(user.getUserId(), aiChatId);

        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // 5. 전체 aiChat 삭제하기
    @DeleteMapping("/asset/{assetId}")
    public ResponseEntity<Void> deleteAllChats(
            @RequestHeader("X-USER-UUID") String uuid,
            @PathVariable Long assetId) {

        User user = userRepository.findByUuid(uuid).orElseThrow();
        aiChatService.deleteAllChatsByAsset(user.getUserId(), assetId);

        return ResponseEntity.noContent().build(); // 204 No Content
    }



}
