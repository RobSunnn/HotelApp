package com.HotelApp.domain.models.binding;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddCommentBindingModel {

    @NotBlank(message = "Please say your name or nickname.")
    @Size(min = 2, max = 30, message = "Your name must be between 2 and 30 characters.")
    private String author;

    @NotBlank(message = "Leave a message here...")
    @Size(min = 2, max = 250, message = "You should at least say Hi...")
    private String commentContent;

    public AddCommentBindingModel() {
    }

    public String getAuthor() {
        return author;
    }

    public AddCommentBindingModel setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public AddCommentBindingModel setCommentContent(String commentContent) {
        this.commentContent = commentContent;
        return this;
    }
}
