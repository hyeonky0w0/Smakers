package com.example.smakersbe.quiz.service;

import com.example.smakersbe.quiz.dto.request.QuizAiAnalysisRequestDTO;
import com.example.smakersbe.quiz.dto.request.QuizAttemptRequestDTO;
import com.example.smakersbe.quiz.dto.request.QuizCreateRequestDTO;
import com.example.smakersbe.quiz.dto.response.*;
import com.example.smakersbe.user.entity.User;

import java.util.List;

public interface QuizService {

    List<QuizCreateResponseDTO> getQuizItemsByQuizSetId(Long quizSetId);

    List<QuizCreateResponseDTO> createQuiz(Long assetId, User user);

    QuizAttemptResponseDTO createQuizAttempt(User user, Long quizSetId, QuizAttemptRequestDTO requestDTO);

    QuizAiAnalysisResponseDTO createQuizAiAnalyze(Long userId, Long quizAttemptId);

    List<QuizHistoryResponseDTO> fetchMyQuizHistory(Long userId, Long assetId, Long quizSetId);

    List<QuizzableAssetListResponseDTO> fetchMyQuizzableAssets(Long userId);

    List<MyQuizSetsByAssetResponseDTO> fetchMyQuizSetsByAsset(Long userId, Long assetId);





}
