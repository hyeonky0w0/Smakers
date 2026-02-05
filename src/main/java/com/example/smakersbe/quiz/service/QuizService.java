package com.example.smakersbe.quiz.service;

import com.example.smakersbe.quiz.dto.request.QuizAiAnalysisRequestDTO;
import com.example.smakersbe.quiz.dto.request.QuizAttemptRequestDTO;
import com.example.smakersbe.quiz.dto.request.QuizCreateRequestDTO;
import com.example.smakersbe.quiz.dto.response.QuizAiAnalysisResponseDTO;
import com.example.smakersbe.quiz.dto.response.QuizAttemptResponseDTO;
import com.example.smakersbe.quiz.dto.response.QuizCreateResponseDTO;
import com.example.smakersbe.quiz.dto.response.QuizHistoryResponseDTO;

import java.util.List;

public interface QuizService {

    List<QuizCreateResponseDTO> createQuiz(QuizCreateRequestDTO requestDTO);

    QuizAttemptResponseDTO createQuizAttempt(QuizAttemptRequestDTO requestDTO);

    QuizAiAnalysisResponseDTO createQuizAiAnalyze(QuizAiAnalysisRequestDTO requestDTO);

    List<QuizHistoryResponseDTO> fetchMyQuizHistory(String uuid, Long assetId);



    }
