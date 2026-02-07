package com.example.smakersbe.quiz.service;

import com.example.smakersbe.quiz.dto.request.QuizAiAnalysisRequestDTO;
import com.example.smakersbe.quiz.dto.request.QuizAttemptRequestDTO;
import com.example.smakersbe.quiz.dto.request.QuizCreateRequestDTO;
import com.example.smakersbe.quiz.dto.response.*;
import com.example.smakersbe.user.entity.User;

import java.util.List;

public interface QuizService {

    List<QuizCreateResponseDTO> createQuiz(QuizCreateRequestDTO requestDTO, User user);

    QuizAttemptResponseDTO createQuizAttempt(QuizAttemptRequestDTO requestDTO);

    QuizAiAnalysisResponseDTO createQuizAiAnalyze(QuizAiAnalysisRequestDTO requestDTO);

    List<QuizHistoryResponseDTO> fetchMyQuizHistory(String uuid, Long assetId);

    List<QuizzableAssetListResponseDTO> fetchMyQuizzableAssets(String uuid);




    }
