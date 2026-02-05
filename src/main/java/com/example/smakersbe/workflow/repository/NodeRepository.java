package com.example.smakersbe.workflow.repository;

import com.example.smakersbe.workflow.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NodeRepository extends JpaRepository<Node, Long> {
    List<Node> findAllByWorkFlow_WorkflowId(Long workflowId);
    void deleteAllByWorkFlow_WorkflowId(Long workflowId);

    Optional<Node> findByNodeIdAndWorkFlow_User_UserId(Long nodeId, Long userId);
    Optional<Node> findByWorkFlow_WorkflowIdAndClientNodeId(
            Long workflowId,
            String clientNodeId
    );

}