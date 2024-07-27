package com.HotelApp.domain.models.binding;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.HotelApp.common.constants.ValidationConstants.*;

public class AddCommentBindingModel {

    @NotBlank(message = NAME_BLANK)
    @Size(min = 2, message = NAME_LENGTH_TOO_SHORT)
    @Size(max = 30, message = NAME_LENGTH_TOO_LONG)
    private String author;

    @NotBlank(message = MESSAGE_BLANK)
    @Size(min = 2, message = MESSAGE_TOO_SHORT)
    @Size(max = 250, message = MESSAGE_TOO_LONG)
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
