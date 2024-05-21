package com.HotelApp.service;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.view.CommentView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    void addCommentToDatabase(AddCommentBindingModel addCommentBindingModel, HotelInfoEntity hotelInfo);

    void approve(Long id);

    void doNotApprove(Long id);

    Page<CommentView> getApproved(Pageable pageable);
}
