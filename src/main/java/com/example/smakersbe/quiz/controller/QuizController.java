package com.example.smakersbe.quiz.controller;

import com.example.smakersbe.quiz.dto.request.QuizAiAnalysisRequestDTO;
import com.example.smakersbe.quiz.dto.request.QuizAttemptRequestDTO;
import com.example.smakersbe.quiz.dto.request.QuizCreateRequestDTO;
import com.example.smakersbe.quiz.dto.response.QuizzableAssetListResponseDTO;
import com.example.smakersbe.quiz.dto.response.QuizAiAnalysisResponseDTO;
import com.example.smakersbe.quiz.dto.response.QuizCreateResponseDTO;
import com.example.smakersbe.quiz.dto.response.QuizAttemptResponseDTO;
import com.example.smakersbe.quiz.dto.response.QuizHistoryResponseDTO;
import com.example.smakersbe.quiz.service.QuizService;
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

    @PostMapping("/create")
    public ResponseEntity<List<QuizCreateResponseDTO>> createQuiz(@RequestBody QuizCreateRequestDTO requestDTO) {
        log.info("퀴즈 생성/조회 요청: user={}, asset={}", requestDTO.getUuid(), requestDTO.getAssetId());

        List<QuizCreateResponseDTO> response = quizService.createQuiz(requestDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/attempts")
    public ResponseEntity<QuizAttemptResponseDTO> createQuizAttempt(@RequestBody QuizAttemptRequestDTO requestDTO) {
        log.info("퀴즈 결과 저장 요청 중");

        QuizAttemptResponseDTO response = quizService.createQuizAttempt(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/ai-analysis")
    public ResponseEntity<QuizAiAnalysisResponseDTO> createQuizAiAnalyze(
            @RequestBody QuizAiAnalysisRequestDTO requestDTO) {
        log.info("퀴즈 결과 ai 분석 요청 중");

        QuizAiAnalysisResponseDTO response = quizService.createQuizAiAnalyze(requestDTO);
        return ResponseEntity.ok(response);
    }

    // 사용자의 특정에셋에 대한 퀴즈 이력 조회
    @GetMapping("/my/history")
    public ResponseEntity<List<QuizHistoryResponseDTO>> fetchMyQuizHistory(
            @RequestParam("uuid") String uuid,
            @RequestParam("assetId") Long assetId
            ) {

        List<QuizHistoryResponseDTO> response = quizService.fetchMyQuizHistory(uuid, assetId);
        return ResponseEntity.ok(response);
    }

    // 사용자가 학습했던 에셋에 대한 퀴즈 썸네일 반환
    @GetMapping("/my/quizzable-assets")
    public ResponseEntity<List<QuizzableAssetListResponseDTO>> fetchMyQuizzableAssets(
            @RequestParam("uuid") String uuid
    ){
        List<QuizzableAssetListResponseDTO> response = quizService.fetchMyQuizzableAssets(uuid);
        return ResponseEntity.ok(response);

    }


}
