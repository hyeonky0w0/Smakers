package com.example.smakersbe.workflow.entity;

import com.example.smakersbe.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//clientNodeID를 워크플로우 문서 내에서 UNQUIE로 두기 위한 수정
@Entity
@Table(
        name = "nodes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_nodes_workflow_client",
                        columnNames = {"workflow_id", "client_node_id"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="node_id", nullable = false, updatable = false)
    private Long nodeId;

    @Column(name="node_name", nullable = false)
    private String nodeName;

    @Column(name="node_content", nullable = false)
    private String nodeContent;

    @Column(name="position_x", nullable = false)
    private Float positionX;

    @Column(name="position_y", nullable = false)
    private Float positionY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="workflow_id", nullable = false)
    private WorkFlow workFlow;

    // 🔑 workflow 안에서만 유니크
    @Column(name="client_node_id", nullable = false, length = 100)
    private String clientNodeId;
}

