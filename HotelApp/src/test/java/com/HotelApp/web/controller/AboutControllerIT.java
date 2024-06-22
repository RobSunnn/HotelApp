package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.CommentEntity;
import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.view.CommentView;
import com.HotelApp.repository.CommentRepository;
import com.HotelApp.service.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Transactional
class AboutControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setup() {
        commentRepository.deleteAll();
    }

    @AfterEach
    void teardown() {
        commentRepository.deleteAll();
    }

    @Test
    void testAboutPageAttributes() throws Exception {
        CommentEntity comment = new CommentEntity()
                .setAuthor("Test Author")
                .setCommentContent("Test Comment Content")
                .setCreated(LocalDateTime.now())
                .setApproved(true);

        List<CommentEntity> comments = new ArrayList<>();
        comments.add(comment);
        commentRepository.saveAll(comments);
//
//        Page<CommentView> mockedPage = new PageImpl<>(comments, PageRequest.of(0, 3), comments.size());
//
//        // Mock the behavior of the service method
//        when(commentService.getApprovedComments(PageRequest.of(0, 3))).thenReturn(mockedPage);


        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attributeExists("addCommentBindingModel"))
                .andExpect(model().attribute("comments", instanceOf(Page.class)))
                .andExpect(model().attribute("comments", hasProperty("content", hasSize(1))));
    }

    @Test
    void testAddValidComment() throws Exception {
        AddCommentBindingModel addCommentBindingModel = new AddCommentBindingModel()
                .setAuthor("Test Author")
                .setCommentContent("Today we finish with tests.");

        mockMvc.perform(post("/about/addComment")
                        .flashAttr("addCommentBindingModel", addCommentBindingModel)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/about"))
                .andExpect(flash().attribute("successCommentMessage", "Thank you for your comment!"));

        assertEquals(1, commentRepository.count());
    }

    @Test
    void testAddInvalidComment() throws Exception {
        AddCommentBindingModel addCommentBindingModel = new AddCommentBindingModel();

        mockMvc.perform(post("/about/addComment")
                        .flashAttr("addCommentBindingModel", addCommentBindingModel)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/about"))
                .andExpect(flash().attributeCount(2))
                .andExpect(result -> {
                    BindingResult resultFromFlash = (BindingResult) result.getFlashMap().get(BindingResult.MODEL_KEY_PREFIX + "addCommentBindingModel");

                    assertEquals(2, resultFromFlash.getErrorCount());
                    assertTrue(resultFromFlash.hasFieldErrors("author"));
                    assertTrue(resultFromFlash.hasFieldErrors("commentContent"));

                });

        assertEquals(0, commentRepository.count());
    }


}