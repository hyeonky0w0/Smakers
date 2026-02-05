package com.example.smakersbe.quiz.repository;

import com.example.smakersbe.quiz.entity.QuizUserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizUserAnswerRepository extends JpaRepository<QuizUserAnswer, Long> {
}
