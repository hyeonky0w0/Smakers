package com.example.smakersbe.quiz.repository;

import com.example.smakersbe.quiz.entity.QuizAttempt;
import com.example.smakersbe.quiz.entity.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {

    Optional<QuizResult> findByQuizAttempt(QuizAttempt attempt);

}
