package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.CommentEntity;
import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.view.CommentView;
import com.HotelApp.repository.CommentRepository;
import com.HotelApp.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private HotelService hotelService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private CommentServiceImpl commentService;

    private AddCommentBindingModel addCommentBindingModel;

    private CommentEntity commentEntity;

    @BeforeEach
    public void setUp() {
        addCommentBindingModel = new AddCommentBindingModel()
                .setAuthor("Test Author")
                .setCommentContent("Testing comment content.");
        commentEntity = new CommentEntity();
    }

    @Test
    public void testAddCommentToDatabase_WithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        commentService.addCommentToDatabase(addCommentBindingModel, bindingResult, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("addCommentBindingModel", addCommentBindingModel);
        verify(redirectAttributes).addFlashAttribute("org.springframework.validation.BindingResult.addCommentBindingModel", bindingResult);
        verifyNoInteractions(commentRepository);
    }

    @Test
    public void testAddCommentToDatabase_NoErrors() {
        commentService.addCommentToDatabase(addCommentBindingModel, bindingResult, redirectAttributes);

        verify(commentRepository, times(1)).save(any(CommentEntity.class));
        verify(redirectAttributes).addFlashAttribute("successCommentMessage", "Thank you for your comment!");
    }

    @Test
    public void testApprove() {
        Long id = 1L;
        when(commentRepository.findById(id)).thenReturn(Optional.of(commentEntity));
        commentService.approve(id);

        assertTrue(commentEntity.getApproved());
        verify(commentRepository).save(commentEntity);
    }

    @Test
    public void testApproveAll() {
        List<CommentEntity> comments = new ArrayList<>();
        CommentEntity comment = new CommentEntity();
        comment.setApproved(false);
        comments.add(comment);

        CommentEntity comment1 = new CommentEntity();
        comment1.setApproved(false);
        comments.add(comment1);

        CommentEntity comment2 = new CommentEntity();
        comment2.setApproved(true);
        comments.add(comment2);

        when(commentRepository.findAll()).thenReturn(comments);

        commentService.approveAll();

        verify(commentRepository, times(1)).save(comment);
        verify(commentRepository, times(1)).save(comment1);
        verify(commentRepository, never()).save(comment2);
    }

    @Test
    public void testDoNotApprove_Success() {
        Long id = 1L;
        when(commentRepository.findById(id)).thenReturn(Optional.of(commentEntity));

        commentService.doNotApprove(id);

        verify(commentRepository).delete(commentEntity);
    }

    @Test
    public void testGetApprovedComments() {
        Pageable pageable = PageRequest.of(0, 10);
        List<CommentEntity> comments = new ArrayList<>();
        comments.add(commentEntity);
        Page<CommentEntity> commentPage = new PageImpl<>(comments);

        when(commentRepository.findByApprovedTrue(pageable)).thenReturn(commentPage);

        Page<CommentView> result = commentService.getApprovedComments(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(commentRepository).findByApprovedTrue(pageable);
    }
}