package com.example.smakersbe.ai.repository;

import com.example.smakersbe.ai.entity.AiChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiChatRepository extends JpaRepository<AiChat, Long> {
}
