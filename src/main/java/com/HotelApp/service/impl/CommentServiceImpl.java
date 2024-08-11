package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.CommentEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.view.CommentView;
import com.HotelApp.repository.CommentRepository;
import com.HotelApp.service.CommentService;
import com.HotelApp.service.HotelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;

import static com.HotelApp.common.constants.SuccessConstants.COMMENT_NOT_FOUND;
import static com.HotelApp.util.ResponseUtil.genericFailResponse;
import static com.HotelApp.util.ResponseUtil.genericSuccessResponse;

@Service
public class CommentServiceImpl implements CommentService {
    private static final String SUCCESS_REDIRECT_URL = "/about";

    private final CommentRepository commentRepository;
    private final HotelService hotelService;

    public CommentServiceImpl(CommentRepository commentRepository, HotelService hotelService) {
        this.commentRepository = commentRepository;
        this.hotelService = hotelService;
    }


    @Override
    public ResponseEntity<?> addCommentToDatabase(AddCommentBindingModel addCommentBindingModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return genericFailResponse(bindingResult);
        }
        HotelInfoEntity hotelInfo = hotelService.getHotelInfo();
        commentRepository.save(mapAsComment(addCommentBindingModel, hotelInfo));

        return genericSuccessResponse(SUCCESS_REDIRECT_URL);
    }

    @Override
    public void approve(Long id) {
        CommentEntity comment = commentRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException(COMMENT_NOT_FOUND));
        comment.setApproved(true);

        commentRepository.save(comment);
    }

    @Override
    public void approveAll() {
        commentRepository
                .findAll()
                .stream()
                .filter(comment -> !comment.getApproved())
                .forEach(comment -> commentRepository.save(comment.setApproved(true)));
    }

    @Override
    public void doNotApprove(Long id) {
        CommentEntity comment = commentRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException(COMMENT_NOT_FOUND));
        commentRepository.delete(comment);
    }

    @Override
    public Page<CommentView> getApprovedComments(Pageable pageable) {
        return commentRepository.findByApprovedTrue(pageable)
                .map(CommentServiceImpl::mapAsCommentView);
    }

    CommentEntity mapAsComment(AddCommentBindingModel addCommentBindingModel, HotelInfoEntity hotelInfo) {
        return new CommentEntity()
                .setCommentContent(addCommentBindingModel.getCommentContent().trim())
                .setApproved(false)
                .setAuthor(addCommentBindingModel.getAuthor().trim())
                .setCreated(LocalDateTime.now())
                .setHotelInfoEntity(hotelInfo);
    }

    private static CommentView mapAsCommentView(CommentEntity comment) {
        return new CommentView()
                .setId(comment.getId())
                .setAuthor(comment.getAuthor())
                .setCommentContent(comment.getCommentContent())
                .setCreated(comment.getCreated());
    }
}
