package com.example.smakersbe.quiz.service;

import com.example.smakersbe.quiz.dto.request.QuizCreateRequestDTO;
import com.example.smakersbe.quiz.dto.response.QuizCreateResponseDTO;

import java.util.List;

public interface QuizService {

    List<QuizCreateResponseDTO> createQuiz(QuizCreateRequestDTO requestDTO);
}
