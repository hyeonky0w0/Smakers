package com.example.smakersbe.quiz.repository;

import com.example.smakersbe.quiz.entity.QuizAttempt;
import com.example.smakersbe.quiz.entity.QuizUserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizUserAnswerRepository extends JpaRepository<QuizUserAnswer, Long> {

    // 유저가 틀린 문제 가져오기
    @Query("SELECT ua FROM QuizUserAnswer ua " +
            "JOIN FETCH ua.quizSetItem " +
            "WHERE ua.quizAttempt.quizAttemptId = :attemptId AND ua.isCorrect = false")
    List<QuizUserAnswer> findWrongAnswersWithItemByAttemptId(@Param("attemptId") Long attemptId);

    List<QuizUserAnswer> findAllByQuizAttempt(QuizAttempt quizAttempt);


}
