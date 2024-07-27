package com.HotelApp.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

import static com.HotelApp.common.constants.ValidationConstants.*;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    @Column(nullable = false)
    @NotBlank(message = NAME_BLANK)
    @Size(min = 2, message = NAME_LENGTH_TOO_SHORT)
    @Size(max = 30, message = NAME_LENGTH_TOO_LONG)
    private String author;

    @Column(name = "comment_content")
    @NotBlank(message = MESSAGE_BLANK)
    @Size(min = 2, message = MESSAGE_TOO_SHORT)
    @Size(max = 250, message = MESSAGE_TOO_LONG)
    private String commentContent;

    @Column(nullable = false)
    private Boolean approved;

    @Column(nullable = false)
    private LocalDateTime created;

    @ManyToOne
    private HotelInfoEntity hotelInfoEntity;

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

    public CommentEntity setHotelInfoEntity(HotelInfoEntity hotelInfoEntity) {
        this.hotelInfoEntity = hotelInfoEntity;
        return this;
    }
}
