package com.example.smakersbe.ai.entity;

import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_chats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AiChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ai_chat_id", nullable = false, updatable = false)
    private Long aiChatId;

    @Column(name="question", columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(name="answer", columnDefinition = "TEXT", nullable = false)
    private String answer;

    @Column(name="is_important", nullable = false)
    private Boolean isImportant=false;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="asset_id", nullable = false)
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", nullable = false)
    private User user;
}
