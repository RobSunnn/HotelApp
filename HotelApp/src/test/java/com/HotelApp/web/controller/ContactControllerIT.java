package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.ContactRequestBindingModel;
import com.HotelApp.service.ContactRequestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ContactControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ContactRequestService contactRequestService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private BindingResult bindingResult;

//    @Test
//    void addAttributes() {
//    }

    @Test
    void contact() throws Exception {

        ContactRequestBindingModel contactRequestBindingModel =
                new ContactRequestBindingModel()
                        .setName("Test")
                                .setEmail("test@test.bg")
                                        .setMessage("Test message");

        mockMvc.perform(post("/contact/contactForm")
                .flashAttr("contactRequestBindingModel", contactRequestBindingModel)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact"));

    }

    @Test
    void contactWithInvalidData() throws Exception {

        ContactRequestBindingModel contactRequestBindingModel =
                new ContactRequestBindingModel()
                        .setName("Test");


        mockMvc.perform(post("/contact/contactForm")
                .flashAttr("contactRequestBindingModel", contactRequestBindingModel)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact"));

        Assertions.assertEquals(2, bindingResult.getAllErrors().size());



    }

    @Test
    void subscribe() {
    }

    @Test
    void sendMail() {
    }
}