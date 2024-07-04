package com.HotelApp.domain.models.view;

import java.time.LocalDateTime;

public class CommentView {

    private Long id;

    private Boolean approved;

    private LocalDateTime created;

    private String commentContent;

    private String author;

    public CommentView() {
    }

    public Long getId() {
        return id;
    }

    public CommentView setId(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getApproved() {
        return approved;
    }

    public CommentView setApproved(Boolean approved) {
        this.approved = approved;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public CommentView setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public CommentView setCommentContent(String commentContent) {
        this.commentContent = commentContent;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public CommentView setAuthor(String author) {
        this.author = author;
        return this;
    }
}
