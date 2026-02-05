package com.example.smakersbe.workflow.repository;

import com.example.smakersbe.workflow.entity.Edge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EdgeRepository extends JpaRepository<Edge, Long> {
    List<Edge> findAllByStartNode_WorkFlow_WorkflowId(Long workflowId);
    void deleteAllByStartNode_WorkFlow_WorkflowId(Long workflowId);
}

