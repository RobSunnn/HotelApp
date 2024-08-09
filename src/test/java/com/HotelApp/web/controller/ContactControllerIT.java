package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import com.HotelApp.repository.ContactRequestRepository;
import com.HotelApp.repository.SubscriberRepository;
import com.HotelApp.service.ContactRequestService;
import com.HotelApp.util.encryptionUtil.EncryptionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.HotelApp.common.constants.BindingConstants.CONTACT_REQUEST_BINDING_MODEL;
import static com.HotelApp.common.constants.BindingConstants.SUBSCRIBER_BINDING_MODEL;
import static com.HotelApp.common.constants.FailConstants.ERRORS;
import static com.HotelApp.common.constants.SuccessConstants.REFERER;
import static com.HotelApp.common.constants.ValidationConstants.*;
import static com.HotelApp.constants.TestConstants.*;
import static com.HotelApp.constants.urlsAndViewsConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ContactControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRequestService contactRequestService;

    @Autowired
    private ContactRequestRepository contactRequestRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Mock
    private BindingResult bindingResult;

    @Autowired
    private EncryptionService encryptionService;


    @BeforeEach
    void setup() {
        contactRequestRepository.deleteAll();
        subscriberRepository.deleteAll();
    }

    @AfterEach
    void teardown() {
        contactRequestRepository.deleteAll();
        subscriberRepository.deleteAll();
    }

    @Test
    void contactRequest_With_ValidData() throws Exception {
        ContactRequestBindingModel contactRequestBindingModel = new ContactRequestBindingModel()
                .setName(USER_FULL_NAME)
                .setEmail(encryptionService.encrypt(TEST_EMAIL))
                .setPhoneNumber(encryptionService.encrypt(TEST_PHONE_NUMBER))
                .setMessage(TEST_MESSAGE);

        // Perform the POST request
        mockMvc.perform(post(CONTACT_REQUEST_URL)
                        .flashAttr(CONTACT_REQUEST_BINDING_MODEL, contactRequestBindingModel)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.redirectUrl").value(CONTACT_URL));

        contactRequestService.sendContactForm(
                contactRequestBindingModel, bindingResult
        );
        assertEquals(2, contactRequestRepository.count());
    }

    @Test
    void contactRequest_With_InvalidData() throws Exception {
        // Create the contact request model with invalid data
        ContactRequestBindingModel contactRequestBindingModel = new ContactRequestBindingModel();

        MvcResult result = mockMvc.perform(post(CONTACT_REQUEST_URL)
                        .flashAttr(CONTACT_REQUEST_BINDING_MODEL, contactRequestBindingModel)
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

        assertEquals(3, errorsList.size());

        assertEquals(MESSAGE_BLANK, errorsList.get(0).get(DEFAULT_MESSAGE).asText());

        assertEquals(INVALID_EMAIL, errorsList.get(1).get(DEFAULT_MESSAGE).asText());

        assertEquals(NAME_BLANK, errorsList.get(2).get(DEFAULT_MESSAGE).asText());

        assertEquals(0, contactRequestRepository.count());
    }


    @Test
    void subscribe_With_ValidEmail() throws Exception {
        AddSubscriberBindingModel addSubscriberBindingModel = new AddSubscriberBindingModel()
                .setSubscriberEmail(encryptionService.encrypt(TEST_EMAIL));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(REFERER, REFERER_URL);

        mockMvc.perform(post(SUBSCRIBE_URL)
                        .flashAttr(SUBSCRIBER_BINDING_MODEL, addSubscriberBindingModel)
                        .header(REFERER, REFERER_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.redirectUrl").value(REFERER_URL));

        assertEquals(1, subscriberRepository.count());
    }

    @Test
    void subscribe_With_InvalidEmail() throws Exception {
        AddSubscriberBindingModel addSubscriberBindingModel = new AddSubscriberBindingModel()
                .setSubscriberEmail(TEST_INVALID_EMAIL);


        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(REFERER, REFERER_URL);

        mockMvc.perform(post(SUBSCRIBE_URL)
                        .flashAttr(SUBSCRIBER_BINDING_MODEL, addSubscriberBindingModel)
                        .header(REFERER, REFERER_URL)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        assertEquals(0, subscriberRepository.count());
    }
}