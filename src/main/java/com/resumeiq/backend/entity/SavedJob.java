package com.resumeiq.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(
    name = "saved_jobs",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_saved_job_user_job",
            columnNames = {"user_id", "job_id"}
        )
    }
)
public class SavedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_saved_job_user"
        )
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "job_id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_saved_job_job"
        )
    )
    private Job job;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public SavedJob() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}