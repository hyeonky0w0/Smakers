package com.example.smakersbe.quiz.repository;

import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.quiz.entity.QuizSet;
import com.example.smakersbe.quiz.entity.QuizSetItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizSetRepository extends JpaRepository<QuizSet, Long> {
    // 푼 시험지 목록 조회
    List<QuizSetItem> findAllByQuizSetId(Long quizSetId);

    // 가장 오래된 풀지 않은 시험지 조회
    Optional<QuizSet> findFirstByAssetAndQuizSetIdNotInOrderByQuizSetIdAsc(Asset asset, List<Long> solvedQuizSetIds);

    // 풀지 않은 시험지 개수 반환
    Long countByAssetAndQuizSetIdNotIn(Asset asset, List<Long>solvedQuizSetIds);

}
