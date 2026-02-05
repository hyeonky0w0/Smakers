package com.example.smakersbe.quiz.controller;

import com.example.smakersbe.quiz.dto.request.QuizAttemptRequestDTO;
import com.example.smakersbe.quiz.dto.request.QuizCreateRequestDTO;
import com.example.smakersbe.quiz.dto.response.QuizCreateResponseDTO;
import com.example.smakersbe.quiz.dto.response.QuizAttemptResponseDTO;
import com.example.smakersbe.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
