package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import com.HotelApp.repository.ContactRequestRepository;
import com.HotelApp.repository.SubscriberRepository;
import com.HotelApp.service.ContactRequestService;
import com.HotelApp.service.SubscriberService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @Mock
    private RedirectAttributes redirectAttributes;

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
                .setEmail("test@mail.bg")
                .setMessage("Hello Testing Form");

        // Perform the POST request
        mockMvc.perform(post("/contact/contactForm")
                        .flashAttr("contactRequestBindingModel", contactRequestBindingModel)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact"))
                .andExpect(flash().attribute("successContactRequestMessage", "Contact Request Send, Thank You!"));

        assertEquals(1, contactRequestRepository.count());
        assertTrue(contactRequestService.sendContactForm(contactRequestBindingModel, bindingResult, redirectAttributes));
        assertEquals(2, contactRequestRepository.count());

    }

    @Test
    void contactRequest_With_InvalidData() throws Exception {
        // Create the contact request model with invalid data
        ContactRequestBindingModel contactRequestBindingModel = new ContactRequestBindingModel().setName("Test");

        mockMvc.perform(post("/contact/contactForm")
                        .flashAttr("contactRequestBindingModel", contactRequestBindingModel)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact"))
                .andExpect(flash().attributeCount(2))
                .andExpect(result -> {

                    BindingResult resultFromFlash = (BindingResult) result
                            .getFlashMap()
                            .get(BindingResult.MODEL_KEY_PREFIX + "contactRequestBindingModel");

                    assertEquals(2, resultFromFlash.getErrorCount());
                    assertTrue(resultFromFlash.hasFieldErrors("message"));
                    assertTrue(resultFromFlash.hasFieldErrors("email"));

                });

        assertEquals(0, contactRequestRepository.count());
    }


    @Test
    void subscribe_With_ValidEmail() throws Exception {
        AddSubscriberBindingModel addSubscriberBindingModel = new AddSubscriberBindingModel()
                .setSubscriberEmail("valid@email.bg");

        String refererUrl = "http://localhost:8080/somePage";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("referer", refererUrl);

        mockMvc.perform(post("/contact/subscribe")
                        .flashAttr("addSubscriberBindingModel", addSubscriberBindingModel)
                        .header("referer", refererUrl)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/somePage"))
                .andExpect(flash().attribute("successSubscribeMessage", "Thank you for subscribing!"));

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
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/somePage"))
                .andExpect(flash().attribute("failMessage", "Please enter valid email."));

        assertEquals(0, subscriberRepository.count());
    }

}