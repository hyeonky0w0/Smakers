package com.example.smakersbe.quiz.repository;

import com.example.smakersbe.quiz.entity.QuizSetItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizSetItemRepository extends JpaRepository<QuizSetItem, Long> {
    @Query("SELECT q FROM QuizSetItem q WHERE q.quizSet.quizSetId = :quizSetId")
    List<QuizSetItem> findAllByQuizSetId(@Param("quizSetId") Long quizSetId);

}
