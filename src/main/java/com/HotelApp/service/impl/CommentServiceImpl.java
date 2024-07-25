package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.CommentEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.view.CommentView;
import com.HotelApp.repository.CommentRepository;
import com.HotelApp.service.CommentService;
import com.HotelApp.service.HotelService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

import static com.HotelApp.common.constants.BindingConstants.BINDING_RESULT_PATH;
import static com.HotelApp.common.constants.BindingConstants.COMMENT_BINDING_MODEL;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final HotelService hotelService;

    public CommentServiceImpl(CommentRepository commentRepository, HotelService hotelService) {
        this.commentRepository = commentRepository;
        this.hotelService = hotelService;
    }


    @Override
    public void addCommentToDatabase(
            AddCommentBindingModel addCommentBindingModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(COMMENT_BINDING_MODEL, addCommentBindingModel);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + COMMENT_BINDING_MODEL, bindingResult);
            return;
        }

        HotelInfoEntity hotelInfo = hotelService.getHotelInfo();
        commentRepository.save(mapAsComment(addCommentBindingModel, hotelInfo));
        redirectAttributes.addFlashAttribute("successCommentMessage", "Thank you for your comment!");
    }

    @Override
    public void approve(Long id) {
        CommentEntity comment = commentRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
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
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }

    @Override
    @Cacheable(value = "commentsCache")
    public Page<CommentView> getApprovedComments(Pageable pageable) {
        System.out.println("PAGEABLE");
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
