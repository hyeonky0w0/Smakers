package com.example.smakersbe.quiz.entity;

import com.example.smakersbe.asset.entity.Asset;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz_sets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class QuizSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="quiz_set_id", nullable = false, updatable = false)
    private Long quizSetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="asset_id", nullable = false)
    private Asset asset;
}
