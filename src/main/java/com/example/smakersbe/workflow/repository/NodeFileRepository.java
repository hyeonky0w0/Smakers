package com.example.smakersbe.workflow.repository;

import com.example.smakersbe.workflow.entity.NodeFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NodeFileRepository extends JpaRepository<NodeFile, Long> {
    List<NodeFile> findAllByNode_NodeIdOrderByNodeFileIdAsc(Long nodeId);

    Optional<NodeFile> findByNodeFileIdAndNode_NodeId(Long nodeFileId, Long nodeId);

    void deleteAllByNode_NodeId(Long nodeId);
}
