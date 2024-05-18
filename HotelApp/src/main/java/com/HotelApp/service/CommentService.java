package com.HotelApp.service;

import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.view.CommentView;

import java.util.List;

public interface CommentService {


    void addCommentToDatabase(AddCommentBindingModel addCommentBindingModel);

    List<CommentView> getAllNotApprovedComments();

    void approve(Long id);

    void doNotApprove(Long id);
}
