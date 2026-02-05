package com.example.smakersbe.quiz.repository;

import com.example.smakersbe.quiz.entity.QuizSetItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizSetItemRepository extends JpaRepository<QuizSetItem, Long> {

    List<QuizSetItem> findAllByQuizSet_QuizSetId(Long quizSetId);
}
