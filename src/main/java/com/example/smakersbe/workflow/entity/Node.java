package com.example.smakersbe.workflow.entity;

import com.example.smakersbe.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nodes")
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

    //클라이언트 노드 아이디
    @Column(name="client_node_id", nullable = false, unique=true, length = 100)
    private String clientNodeId;






}
