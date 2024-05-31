package com.HotelApp.service;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.view.CommentView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    void addCommentToDatabase(AddCommentBindingModel addCommentBindingModel);

    void approve(Long id);

    void doNotApprove(Long id);

    Page<CommentView> getApprovedComments(Pageable pageable);

    void approveAll();

}
