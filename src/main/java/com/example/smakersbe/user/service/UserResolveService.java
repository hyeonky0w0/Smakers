package com.example.smakersbe.user.service;

import com.example.smakersbe.user.entity.User;
import com.example.smakersbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserResolveService {

    private final UserRepository userRepository;

    @Transactional
    public User getOrCreateByUuid(String uuid) {
        return userRepository.findByUuid(uuid)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .uuid(uuid)
                                .createdAt(LocalDateTime.now())
                                .build()
                ));
    }
}

