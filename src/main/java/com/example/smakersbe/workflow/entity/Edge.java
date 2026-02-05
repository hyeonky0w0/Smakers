package com.example.smakersbe.workflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "edges",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_edges_workflow_client",
                        columnNames = {"workflow_id", "client_edge_id"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Edge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="edge_id", nullable = false, updatable = false)
    private Long edgeId;

    // 어떤 workflow에 속한 edge인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    private WorkFlow workFlow;

    // 시작 노드
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_node_id", nullable = false)
    private Node startNode;

    // 종료 노드
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_node_id", nullable = false)
    private Node endNode;

    // workflow 안에서만 유니크
    @Column(name="client_edge_id", nullable = false, length = 100)
    private String clientEdgeId;
}

