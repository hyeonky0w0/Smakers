package com.example.smakersbe.workflow.service;

import com.example.smakersbe.workflow.dto.response.NodeFileResponse;
import com.example.smakersbe.workflow.entity.Node;
import com.example.smakersbe.workflow.entity.NodeFile;
import com.example.smakersbe.workflow.repository.NodeFileRepository;
import com.example.smakersbe.workflow.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NodeFileService {

    private final NodeRepository nodeRepository;
    private final NodeFileRepository nodeFileRepository;
    private final NodeFileStorage nodeFileStorage; // S3 연결(또는 Dummy)

    /** workflowId + clientNodeId로 Node 찾고, user 소유권 검증 */
    private Node getOwnedNode(Long userId, Long workflowId, String clientNodeId) {
        Node node = nodeRepository.findByWorkFlow_WorkflowIdAndClientNodeId(workflowId, clientNodeId)
                .orElseThrow(() -> new IllegalArgumentException("Node not found"));

        if (!node.getWorkFlow().getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("No permission");
        }
        return node;
    }

    public List<NodeFileResponse> list(Long userId, Long workflowId, String clientNodeId) {
        Node node = getOwnedNode(userId, workflowId, clientNodeId);

        return nodeFileRepository.findAllByNode_NodeIdOrderByNodeFileIdAsc(node.getNodeId())
                .stream()
                .map(f -> new NodeFileResponse(f.getNodeFileId(), f.getNodeFileName(), f.getNodeFileUrl()))
                .toList();
    }

    public NodeFileResponse upload(Long userId, Long workflowId, String clientNodeId, MultipartFile file, String name) {
        Node node = getOwnedNode(userId, workflowId, clientNodeId);

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("file is required");
        }

        // S3 키 생성에 nodeId(Long) 쓸지, clientNodeId(String) 쓸지 선택 가능
        String url = nodeFileStorage.upload(file, userId, node.getNodeId());

        String fileName = (name == null || name.isBlank())
                ? (file.getOriginalFilename() == null ? "file" : file.getOriginalFilename())
                : name;

        NodeFile saved = nodeFileRepository.save(
                NodeFile.builder()
                        .node(node)
                        .nodeFileName(fileName)
                        .nodeFileUrl(url)
                        .build()
        );

        return new NodeFileResponse(saved.getNodeFileId(), saved.getNodeFileName(), saved.getNodeFileUrl());
    }

    public NodeFileResponse update(
            Long userId,
            Long workflowId,
            String clientNodeId,
            Long nodeFileId,
            MultipartFile file,
            String name
    ) {
        Node node = getOwnedNode(userId, workflowId, clientNodeId);

        NodeFile nodeFile = nodeFileRepository.findByNodeFileIdAndNode_NodeId(nodeFileId, node.getNodeId())
                .orElseThrow(() -> new IllegalArgumentException("NodeFile not found"));

        // 1) 이름 변경
        if (name != null && !name.isBlank()) {
            nodeFile.setNodeFileName(name);
        }

        // 2) 파일 교체
        if (file != null && !file.isEmpty()) {
            nodeFileStorage.deleteByUrl(nodeFile.getNodeFileUrl());

            String newUrl = nodeFileStorage.upload(file, userId, node.getNodeId());
            nodeFile.setNodeFileUrl(newUrl);

            if (name == null || name.isBlank()) {
                nodeFile.setNodeFileName(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename());
            }
        }

        return new NodeFileResponse(nodeFile.getNodeFileId(), nodeFile.getNodeFileName(), nodeFile.getNodeFileUrl());
    }

    public void delete(Long userId, Long workflowId, String clientNodeId, Long nodeFileId) {
        Node node = getOwnedNode(userId, workflowId, clientNodeId);

        NodeFile nodeFile = nodeFileRepository.findByNodeFileIdAndNode_NodeId(nodeFileId, node.getNodeId())
                .orElseThrow(() -> new IllegalArgumentException("NodeFile not found"));

        nodeFileStorage.deleteByUrl(nodeFile.getNodeFileUrl());
        nodeFileRepository.delete(nodeFile);
    }
}
