package com.example.smakersbe.workflow.entity;

import com.example.smakersbe.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "workflows")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="workflow_id", nullable = false, updatable = false)
    private Long workflowId;

    @Column(name="workflow_name", nullable = false)
    private String workflowName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", nullable = false)
    private User user;

    // 문서 저장 충돌 방지 (PUT 저장 성공 시 +1)
    @Column(name = "revision", nullable = false)
    private Long revision = 1L;

    // 프론트 meta.schemaVersion 대응
    @Column(name = "schema_version", nullable = false)
    private Integer schemaVersion = 1;

    // 생성 시각 (생성일 정렬용)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 마지막 수정 시각 (autosave 시 갱신)
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 소프트 삭제 (삭제되면 now, 조회 시 where로 걸러야 함), 지금은 hard delete로 구현
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.revision == null) this.revision = 1L;
        if (this.schemaVersion == null) this.schemaVersion = 1;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
