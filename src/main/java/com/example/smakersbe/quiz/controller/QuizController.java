package com.example.smakersbe.quiz.controller;

import com.example.smakersbe.quiz.dto.request.QuizAiAnalysisRequestDTO;
import com.example.smakersbe.quiz.dto.request.QuizAttemptRequestDTO;
import com.example.smakersbe.quiz.dto.request.QuizCreateRequestDTO;
import com.example.smakersbe.quiz.dto.response.*;
import com.example.smakersbe.quiz.service.QuizService;
import com.example.smakersbe.user.entity.User;
import com.example.smakersbe.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@Slf4j
public class QuizController {

    private final QuizService quizService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<List<QuizCreateResponseDTO>> createQuiz(
            @RequestHeader("X-USER-UUID") String uuid,
            @RequestBody QuizCreateRequestDTO requestDTO) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("유저 없음"));

        List<QuizCreateResponseDTO> response = quizService.createQuiz(requestDTO, user);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/attempts")
    public ResponseEntity<QuizAttemptResponseDTO> createQuizAttempt(
            @RequestHeader("X-USER-UUID") String uuid,
            @RequestBody QuizAttemptRequestDTO requestDTO) {
        log.info("퀴즈 결과 저장 요청 중");

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("유저 없음"));

        QuizAttemptResponseDTO response = quizService.createQuizAttempt(user, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/ai-analysis")
    public ResponseEntity<QuizAiAnalysisResponseDTO> createQuizAiAnalyze(
            @RequestHeader("X-USER-UUID") String uuid,
            @RequestBody QuizAiAnalysisRequestDTO requestDTO) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("유저 없음"));
        Long userId = user.getUserId();

        QuizAiAnalysisResponseDTO response = quizService.createQuizAiAnalyze(userId, requestDTO);
        return ResponseEntity.ok(response);
    }

    // 사용자의 특정에셋+ 특정 퀴즈에 대한 히스토리(성적표)조회
    @GetMapping("/my/history")
    public ResponseEntity<List<QuizHistoryResponseDTO>> fetchMyQuizHistory(
            @RequestHeader("X-USER-UUID") String uuid,
            @RequestParam("assetId") Long assetId,
            @RequestParam("quizSetId") Long quizSetId

    ) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("유저 없음"));
        Long userId = user.getUserId();

        List<QuizHistoryResponseDTO> response = quizService.fetchMyQuizHistory(userId, assetId, quizSetId);
        return ResponseEntity.ok(response);
    }

    // 사용자가 학습했던 에셋에 대한 퀴즈 썸네일 반환
    @GetMapping("/my/quizzable-assets")
    public ResponseEntity<List<QuizzableAssetListResponseDTO>> fetchMyQuizzableAssets(
            @RequestHeader("X-USER-UUID") String uuid
    ){

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("유저 없음"));
        Long userId = user.getUserId();
        List<QuizzableAssetListResponseDTO> response = quizService.fetchMyQuizzableAssets(userId);
        return ResponseEntity.ok(response);

    }

    // 특정 에셋에 대한 퀴즈 이력 quiz_set_id 조회
    @GetMapping("{assetId}/my/history")
    public ResponseEntity<List<MyQuizSetsByAssetResponseDTO>> fetchMyQuizSetsByAsset(
            @RequestHeader("X-USER-UUID") String uuid,
            @PathVariable Long assetId
    ) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("유저 없음"));
        Long userId = user.getUserId();

        List<MyQuizSetsByAssetResponseDTO> response = quizService.fetchMyQuizSetsByAsset(userId, assetId);
        return ResponseEntity.ok(response);
    }



}
