package com.example.smakersbe.workflow.repository;

import com.example.smakersbe.workflow.entity.WorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkFlowRepository extends JpaRepository<WorkFlow, Long> {
    List<WorkFlow> findAllByUser_UserIdOrderByUpdatedAtDesc(Long userId);
    Optional<WorkFlow> findByWorkflowIdAndUser_UserId(Long workflowId, Long userId);
}

