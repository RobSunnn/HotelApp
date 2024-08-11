package com.HotelApp.service;

import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.view.CommentView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface CommentService {

    ResponseEntity<?> addCommentToDatabase(AddCommentBindingModel addCommentBindingModel, BindingResult bindingResult);

    void approve(Long id);

    void approveAll();

    void doNotApprove(Long id);

    Page<CommentView> getApprovedComments(Pageable pageable);

}
