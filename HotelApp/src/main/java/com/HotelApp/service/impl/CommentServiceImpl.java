package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.CommentEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.view.CommentView;
import com.HotelApp.repository.CommentRepository;
import com.HotelApp.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.HotelApp.config.SecurityConfiguration.modelMapper;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void addCommentToDatabase(AddCommentBindingModel addCommentBindingModel, HotelInfoEntity hotelInfo) {
        commentRepository.save(mapAsComment(addCommentBindingModel, hotelInfo));
    }

    @Override
    public void approve(Long id) {
        CommentEntity comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setApproved(true);
        commentRepository.save(comment);
    }

    @Override
    public void doNotApprove(Long id) {
        CommentEntity comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));

        commentRepository.delete(comment);
    }

    @Override
    public Page<CommentView> getApproved(Pageable pageable) {
        return commentRepository.findByApprovedTrue(pageable)
                .map(CommentServiceImpl::mapAsCommentView);
    }

    private CommentEntity mapAsComment(AddCommentBindingModel addCommentBindingModel, HotelInfoEntity hotelInfo) {
        return new CommentEntity()
                .setCommentContent(addCommentBindingModel.getCommentContent())
                .setApproved(false)
                .setAuthor(addCommentBindingModel.getAuthor())
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
