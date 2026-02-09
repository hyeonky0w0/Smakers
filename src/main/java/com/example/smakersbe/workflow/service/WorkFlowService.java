package com.example.smakersbe.workflow.service;

import com.example.smakersbe.user.entity.User;
import com.example.smakersbe.workflow.dto.request.WorkFlowCreateRequest;
import com.example.smakersbe.workflow.dto.request.WorkFlowSaveRequest;
import com.example.smakersbe.workflow.dto.response.WorkFlowDetailResponse;
import com.example.smakersbe.workflow.dto.response.WorkFlowListItemResponse;
import com.example.smakersbe.workflow.dto.response.WorkFlowSaveResponse;
import com.example.smakersbe.workflow.entity.Edge;
import com.example.smakersbe.workflow.entity.Node;
import com.example.smakersbe.workflow.entity.WorkFlow;
import com.example.smakersbe.workflow.repository.EdgeRepository;
import com.example.smakersbe.workflow.repository.NodeRepository;
import com.example.smakersbe.workflow.repository.WorkFlowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.smakersbe.workflow.dto.request.WorkFlowRenameRequest;
import com.example.smakersbe.workflow.dto.response.WorkFlowRenameResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkFlowService {

    private final WorkFlowRepository workFlowRepository;
    private final NodeRepository nodeRepository;
    private final EdgeRepository edgeRepository;

    public List<WorkFlowListItemResponse> list(Long userId) {
        return workFlowRepository.findAllByUser_UserIdOrderByUpdatedAtDesc(userId) // 정렬 기준 변경
                .stream()
                .map(w -> new WorkFlowListItemResponse(
                        w.getWorkflowId(),
                        w.getWorkflowName(),
                        w.getSchemaVersion(),
                        w.getCreatedAt(),
                        w.getUpdatedAt()
                ))
                .toList();
    }


    public WorkFlowDetailResponse detail(Long userId, Long workflowId) {
        WorkFlow wf = workFlowRepository.findByWorkflowIdAndUser_UserId(workflowId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found"));

        List<Node> nodes = nodeRepository.findAllByWorkFlow_WorkflowId(workflowId);

        // edges: workflowId로 한번에 뽑기 위해 startNode 기준으로 조회
        List<Edge> edges = edgeRepository.findAllByStartNode_WorkFlow_WorkflowId(workflowId);

        var nodeDtos = nodes.stream()
                .map(n -> new WorkFlowDetailResponse.NodeDto(
                        n.getClientNodeId(),
                        n.getNodeName(),
                        n.getNodeContent(),
                        n.getPositionX(),
                        n.getPositionY()
                ))
                .toList();

        var edgeDtos = edges.stream()
                .map(e -> new WorkFlowDetailResponse.EdgeDto(
                        e.getClientEdgeId(),
                        e.getStartNode().getClientNodeId(),
                        e.getEndNode().getClientNodeId()
                ))
                .toList();

        return new WorkFlowDetailResponse(
                wf.getWorkflowId(),
                wf.getWorkflowName(),
                new WorkFlowDetailResponse.WorkFlowData(nodeDtos, edgeDtos),
                wf.getRevision()
        );
    }

    public WorkFlowDetailResponse create(Long userId, WorkFlowCreateRequest req, User userEntity) {
        WorkFlow wf = WorkFlow.builder()
                .workflowName(req.name() == null ? "Untitled" : req.name())
                .user(userEntity) // 보통 userRepo로 조회해서 넣음
                .revision(1L)
                .schemaVersion(1)
                .build();
        workFlowRepository.save(wf);

        // 처음엔 빈 data 가능
        if (req.data() != null) {
            overwriteData(wf, req.data());
        }

        return detail(userId, wf.getWorkflowId());
    }

    public void delete(Long userId, Long workflowId) {
        WorkFlow wf = workFlowRepository.findByWorkflowIdAndUser_UserId(workflowId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found"));

        // FK 때문에 node/edge 먼저 지우는 게 안전
        edgeRepository.deleteAllByStartNode_WorkFlow_WorkflowId(workflowId);
        nodeRepository.deleteAllByWorkFlow_WorkflowId(workflowId);

        workFlowRepository.delete(wf);
    }

    public WorkFlowSaveResponse save(Long userId, Long workflowId, WorkFlowSaveRequest req) {
        WorkFlow wf = workFlowRepository.findByWorkflowIdAndUser_UserId(workflowId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found"));

        // ✅ revision 충돌 방지 (Optimistic lock처럼 동작)
        if (req.revision() == null || !req.revision().equals(wf.getRevision())) {
            throw new IllegalStateException("Revision conflict");
        }

        if (req.name() != null && !req.name().isBlank()) {
            wf.setWorkflowName(req.name());
        }

        if (req.data() != null) {
            overwriteData(wf, req.data());
        }

        // 저장 성공 시 revision +1
        wf.setRevision(wf.getRevision() + 1);
        // updatedAt은 @PreUpdate로 올라가지만, wf 값 변경이 있으니 보통 OK
        workFlowRepository.save(wf);

        return new WorkFlowSaveResponse(true, wf.getRevision(), wf.getUpdatedAt());
    }

    /**
     * 문서 전체 덮어쓰기 전략:
     * - 기존 nodes/edges 전부 삭제
     * - nodes 먼저 삽입 (clientNodeId 기반)
     * - edges 삽입 시 source/target clientNodeId로 Node 찾아서 FK 설정
     */
    private void overwriteData(WorkFlow wf, WorkFlowDetailResponse.WorkFlowData data) {
        Long workflowId = wf.getWorkflowId();

        edgeRepository.deleteAllByStartNode_WorkFlow_WorkflowId(workflowId);
        nodeRepository.deleteAllByWorkFlow_WorkflowId(workflowId);

        // ✅ delete를 DB에 먼저 반영
        edgeRepository.flush();
        nodeRepository.flush();

        // 1) nodes insert
        Map<String, Node> nodeMap = new HashMap<>();
        for (var n : data.nodes()) {
            Node node = Node.builder()
                    .workFlow(wf)
                    .clientNodeId(n.id())
                    .nodeName(n.name())
                    .nodeContent(n.content())
                    .positionX(n.positionX())
                    .positionY(n.positionY())
                    .build();
            nodeRepository.save(node);
            nodeMap.put(n.id(), node);
        }

        // 2) edges insert
        for (var e : data.edges()) {
            Node start = nodeMap.get(e.source());
            Node end = nodeMap.get(e.target());
            if (start == null || end == null) {
                throw new IllegalArgumentException("Edge references missing node: " + e.id());
            }
            Edge edge = Edge.builder()
                    .workFlow(wf)
                    .clientEdgeId(e.id())
                    .startNode(start)
                    .endNode(end)
                    .build();
            edgeRepository.save(edge);
        }
    }

    public WorkFlowRenameResponse rename(Long userId, Long workflowId, WorkFlowRenameRequest req) {
        WorkFlow wf = workFlowRepository.findByWorkflowIdAndUser_UserId(workflowId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found"));

        if (req == null || req.name() == null || req.name().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }

        // ✅ revision 충돌 방지
        if (req.revision() == null || !req.revision().equals(wf.getRevision())) {
            throw new IllegalStateException("Revision conflict");
        }

        // (선택) 너무 긴 이름 방지
        String newName = req.name().trim();
        if (newName.length() > 100) {
            throw new IllegalArgumentException("name is too long (max 100)");
        }

        wf.setWorkflowName(newName);

        // 이름만 바뀌어도 문서 변경이므로 revision +1 (추천)
        wf.setRevision(wf.getRevision() + 1);

        // updatedAt은 @PreUpdate로 갱신됨
        workFlowRepository.save(wf);

        return new WorkFlowRenameResponse(
                true,
                wf.getWorkflowId(),
                wf.getWorkflowName(),
                wf.getRevision(),
                wf.getUpdatedAt()
        );
    }
}
