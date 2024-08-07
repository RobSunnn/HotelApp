package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.CommentEntity;
import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.repository.CommentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.HotelApp.common.constants.AppConstants.COMMENTS;
import static com.HotelApp.common.constants.BindingConstants.COMMENT_BINDING_MODEL;
import static com.HotelApp.common.constants.SuccessConstants.COMMENT_SUCCESS;
import static com.HotelApp.common.constants.SuccessConstants.COMMENT_SUCCESS_MESSAGE;
import static com.HotelApp.service.constants.TestConstants.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
                .setAuthor(TEST_AUTHOR)
                .setCommentContent(TEST_COMMENT_CONTENT)
                .setCreated(LocalDateTime.now())
                .setApproved(true);

        List<CommentEntity> comments = new ArrayList<>();
        comments.add(comment);
        commentRepository.saveAll(comments);

        mockMvc.perform(get(ABOUT_URL))
                .andExpect(status().isOk())
                .andExpect(view().name("about"))
                .andExpect(model().attributeExists(COMMENTS))
                .andExpect(model().attributeExists(COMMENT_BINDING_MODEL))
                .andExpect(model().attribute(COMMENTS, instanceOf(Page.class)))
                .andExpect(model().attribute(COMMENTS, hasProperty("content", hasSize(1))));
    }

    @Test
    void testAddValidComment() throws Exception {
        AddCommentBindingModel addCommentBindingModel = new AddCommentBindingModel()
                .setAuthor(TEST_AUTHOR)
                .setCommentContent(TEST_COMMENT_CONTENT);

        mockMvc.perform(post(ADD_COMMENT_URL)
                        .flashAttr(COMMENT_BINDING_MODEL, addCommentBindingModel)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ABOUT_URL))
                .andExpect(flash().attribute(COMMENT_SUCCESS, COMMENT_SUCCESS_MESSAGE));

        assertEquals(1, commentRepository.count());
    }

    @Test
    void testAddInvalidComment() throws Exception {
        AddCommentBindingModel addCommentBindingModel = new AddCommentBindingModel();

        mockMvc.perform(post(ADD_COMMENT_URL)
                        .flashAttr(COMMENT_BINDING_MODEL, addCommentBindingModel)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ABOUT_URL))
                .andExpect(flash().attributeCount(2))
                .andExpect(result -> {
                    BindingResult resultFromFlash = (BindingResult) result.getFlashMap().get(BindingResult.MODEL_KEY_PREFIX + "addCommentBindingModel");

                    assertEquals(2, resultFromFlash.getErrorCount());
                    assertTrue(resultFromFlash.hasFieldErrors(AUTHOR));
                    assertTrue(resultFromFlash.hasFieldErrors(COMMENT_CONTENT));

                });

        assertEquals(0, commentRepository.count());
    }
}