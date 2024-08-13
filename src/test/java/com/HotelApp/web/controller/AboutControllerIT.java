package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.CommentEntity;
import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.repository.CommentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.HotelApp.common.constants.AppConstants.COMMENTS;
import static com.HotelApp.common.constants.BindingConstants.COMMENT_BINDING_MODEL;
import static com.HotelApp.common.constants.FailConstants.ERRORS;
import static com.HotelApp.common.constants.ValidationConstants.MESSAGE_BLANK;
import static com.HotelApp.common.constants.ValidationConstants.NAME_BLANK;
import static com.HotelApp.constants.TestConstants.*;
import static com.HotelApp.constants.urlsAndViewsConstants.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
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
                .andExpect(view().name(ABOUT_PAGE_VIEW))
                .andExpect(model().attributeExists(COMMENTS))
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.redirectUrl").value(ABOUT_URL));

        assertEquals(1, commentRepository.count());
    }

    @Test
    void testAddInvalidComment() throws Exception {
        AddCommentBindingModel addCommentBindingModel = new AddCommentBindingModel();

        MvcResult result = mockMvc.perform(post(ADD_COMMENT_URL)
                        .flashAttr(COMMENT_BINDING_MODEL, addCommentBindingModel)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonResponse);

        // Extract and assert on an error array
        JsonNode errorsNode = jsonNode.get(ERRORS);
        assertNotNull(errorsNode);
        assertTrue(errorsNode.isArray());

        List<JsonNode> errorsList = new ArrayList<>();
        errorsNode.forEach(errorsList::add);
        errorsList.sort(Comparator.comparing(node -> node.get(DEFAULT_MESSAGE).asText()));

        assertEquals(2, errorsList.size());

        assertEquals(MESSAGE_BLANK, errorsList.get(0).get(DEFAULT_MESSAGE).asText());
        assertEquals(NAME_BLANK, errorsList.get(1).get(DEFAULT_MESSAGE).asText());

        assertEquals(0, commentRepository.count());
    }
}