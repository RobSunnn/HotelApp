package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.CommentEntity;
import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.view.CommentView;
import com.HotelApp.repository.CommentRepository;
import com.HotelApp.service.CommentService;
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
    public void addCommentToDatabase(AddCommentBindingModel addCommentBindingModel) {

        commentRepository.save(mapAsComment(addCommentBindingModel));
    }

    @Override
    public List<CommentView> getAllNotApprovedComments() {
        List<CommentEntity> allNotApprovedComments = commentRepository.findByApprovedFalse();
        List<CommentView> notApprovedCommentsView = new ArrayList<>();

        for (CommentEntity comment : allNotApprovedComments) {
            notApprovedCommentsView.add(modelMapper().map(comment, CommentView.class));
        }

        return notApprovedCommentsView;
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

    private CommentEntity mapAsComment(AddCommentBindingModel addCommentBindingModel) {
        return new CommentEntity()
                .setCommentContent(addCommentBindingModel.getCommentContent())
                .setApproved(false)
                .setAuthor(addCommentBindingModel.getAuthor())
                .setCreated(LocalDateTime.now());
    }
}
