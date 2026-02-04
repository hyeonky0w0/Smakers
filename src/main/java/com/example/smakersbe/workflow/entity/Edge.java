package com.example.smakersbe.workflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "edges")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Edge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="edge_id", nullable = false, updatable = false)
    private Long edgeId;

    // 시작 노드 참조 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_node_id", nullable = false)
    private Node startNode;

    // 종료 노드 참조 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_node_id", nullable = false)
    private Node endNode;

    // 클라이언트 엣지 아이디
    @Column(name="client_edge_id", nullable = false, unique=true, length = 100)
    private String clientEdgeId;

}
