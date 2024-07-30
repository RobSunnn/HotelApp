package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.RoleEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import com.HotelApp.repository.ContactRequestRepository;
import com.HotelApp.repository.OnlineReservationRepository;
import com.HotelApp.repository.SubscriberRepository;
import com.HotelApp.repository.UserRepository;
import com.HotelApp.service.ContactRequestService;
import com.HotelApp.service.HotelService;
import com.HotelApp.service.MailService;
import com.HotelApp.service.impl.AppUserDetailsService;
import com.HotelApp.service.impl.CustomUser;
import com.HotelApp.service.impl.UserTransformationService;
import com.HotelApp.util.encryptionUtil.EncryptionService;
import com.HotelApp.util.encryptionUtil.KeyService;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.HotelApp.config.ApplicationBeanConfiguration.passwordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .setName("Valid Name")
                .setEmail(encryptionService.encrypt("test@mail.bg"))
                .setPhoneNumber(encryptionService.encrypt("0888 888 888"))
                .setMessage("Hello Testing Form");

        // Perform the POST request
        mockMvc.perform(post("/contact/contactForm")
                        .flashAttr("contactRequestBindingModel", contactRequestBindingModel)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.redirectUrl").value("/contact"));

        contactRequestService.sendContactForm(
                contactRequestBindingModel, bindingResult
        );
        assertEquals(2, contactRequestRepository.count());
    }

    @Test
    void contactRequest_With_InvalidData() throws Exception {
        // Create the contact request model with invalid data
        ContactRequestBindingModel contactRequestBindingModel = new ContactRequestBindingModel();

        MvcResult result = mockMvc.perform(post("/contact/contactForm")
                        .flashAttr("contactRequestBindingModel", contactRequestBindingModel)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonResponse);

        // Extract and assert on an error array
        JsonNode errorsNode = jsonNode.get("errors");
        assertNotNull(errorsNode);
        assertTrue(errorsNode.isArray());

        List<JsonNode> errorsList = new ArrayList<>();
        errorsNode.forEach(errorsList::add);
        errorsList.sort(Comparator.comparing(node -> node.get("defaultMessage").asText()));

        assertEquals(4, errorsList.size());

        assertEquals("Email should be provided.",
                errorsList.get(0).get("defaultMessage").asText());

        assertEquals("Leave a message here...",
                errorsList.get(1).get("defaultMessage").asText());

        assertEquals("Please enter real email...",
                errorsList.get(2).get("defaultMessage").asText());

        assertEquals("Please, provide a name.",
                errorsList.get(3).get("defaultMessage").asText());

        assertEquals(0, contactRequestRepository.count());
    }


    @Test
    void subscribe_With_ValidEmail() throws Exception {
        AddSubscriberBindingModel addSubscriberBindingModel = new AddSubscriberBindingModel()
                .setSubscriberEmail(encryptionService.encrypt("valid@email.bg"));

        String refererUrl = "/somePage";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("referer", refererUrl);

        mockMvc.perform(post("/contact/subscribe")
                        .flashAttr("addSubscriberBindingModel", addSubscriberBindingModel)
                        .header("referer", refererUrl)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.redirectUrl").value("/somePage"));

        assertEquals(1, subscriberRepository.count());
    }

    @Test
    void subscribe_With_InvalidEmail() throws Exception {
        AddSubscriberBindingModel addSubscriberBindingModel = new AddSubscriberBindingModel()
                .setSubscriberEmail("invalid-email.bg");

        String refererUrl = "http://localhost:8080/somePage";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("referer", refererUrl);

        mockMvc.perform(post("/contact/subscribe")
                        .flashAttr("addSubscriberBindingModel", addSubscriberBindingModel)
                        .header("referer", refererUrl)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        assertEquals(0, subscriberRepository.count());
    }
}