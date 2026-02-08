package com.example.smakersbe.workflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "node_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodeFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="node_file_id", nullable = false, updatable = false)
    private Long nodeFileId;

    @Column(name="node_file_name", nullable = false)
    private String nodeFileName;

    @Column(name="node_file_url", length = 1000, nullable = false)
    private String nodeFileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="node_id", nullable = false)
    private Node node;
}
