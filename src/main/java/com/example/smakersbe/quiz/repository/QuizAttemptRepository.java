package com.example.smakersbe.quiz.repository;

import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.quiz.entity.QuizAttempt;
import com.example.smakersbe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    // 특정 유저가 특정 에셋에서 마지막으로 푼 시험 기록 찾기
    @Query("SELECT MAX(qa.quizSet.quizSetId) FROM QuizAttempt qa " +
            "WHERE qa.user = :user AND qa.quizSet.asset = :asset")
    Long findLastQuizSetIdByUserAndAsset(@Param("user") User user, @Param("asset") Asset asset);

    @Query("SELECT qa.quizSet.quizSetId FROM QuizAttempt qa WHERE qa.user = :user AND qa.quizSet.asset = :asset")
    List<Long> findQuizSetIdsByUserAndAsset(@Param("user") User user, @Param("asset") Asset asset);

    @Query("SELECT qa FROM QuizAttempt qa " +
            "JOIN qa.quizSet qs " +
            "JOIN qs.asset a " +
            "WHERE qa.user.uuid = :uuid AND a.assetId = :assetId " +
            "ORDER BY qa.quizAttemptId DESC")
    List<QuizAttempt> findAllByUuidAndAssetId(@Param("uuid") String uuid, @Param("assetId") Long assetId);

    List<QuizAttempt> findAllByUser_UserIdAndQuizSet_Asset_AssetIdAndQuizSet_QuizSetIdOrderByCreatedAtDesc(Long userId, Long assetId, Long quizSetId);

    @Query("SELECT DISTINCT qa.quizSet.quizSetId FROM QuizAttempt qa " +
            "WHERE qa.user.userId = :userId " +
            "AND qa.quizSet.asset.assetId = :assetId")
    List<Long> findSolvedQuizSetIds(@Param("userId") Long userId, @Param("assetId") Long assetId);
}
