package com.HotelApp.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    @Column
    private Boolean approved;

    @Column
    private LocalDateTime created;

    @Column(name = "comment_content", columnDefinition = "TEXT")
    private String commentContent;

    @Column
    private String author;

    public CommentEntity() {
    }

    public Boolean getApproved() {
        return approved;
    }

    public CommentEntity setApproved(Boolean approved) {
        this.approved = approved;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public CommentEntity setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public CommentEntity setCommentContent(String commentContent) {
        this.commentContent = commentContent;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public CommentEntity setAuthor(String author) {
        this.author = author;
        return this;
    }
}
