package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.domain.models.service.CustomUser;
import com.HotelApp.service.impl.AppUserDetailsService;
import com.HotelApp.util.encryptionUtil.EncryptionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.HotelApp.config.ApplicationSecurityConfiguration.passwordEncoder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthenticationRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserDetailsService userDetailsService;

    @Autowired
    private EncryptionService encryptionService;

    @Test
    @WithAnonymousUser
    public void testLoginInvalidCredentials() throws Exception {
        mockMvc.perform(post("/users/login")
                        .param("encryptedEmail", encryptionService.encrypt("test@example.com"))
                        .param("encryptedPass", encryptionService.encrypt("password"))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    @WithAnonymousUser
    public void testLogin_Success() throws Exception {
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(new CustomUser(
                "test@example.com",
                passwordEncoder().encode("password"),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                "Test User"
        ));
        mockMvc.perform(post("/users/login")
                        .param("encryptedEmail", encryptionService.encrypt("test@example.com"))
                        .param("encryptedPass", encryptionService.encrypt("password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login success"));
    }

    @Test
    @WithAnonymousUser
    public void registerUser_Success() throws Exception {

        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel()
                .setFirstName("Test")
                .setLastName("User")
                .setEmail(encryptionService.encrypt("testov@email.bg"))
                .setAge(33)
                .setPassword(encryptionService.encrypt("testing"))
                .setConfirmPassword(encryptionService.encrypt("testing"));


        mockMvc.perform(post("/users/register")
                        .flashAttr("userRegisterBindingModel", userRegisterBindingModel)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.redirectUrl", is("/users/registrationSuccess")));
    }

    @Test
    @WithAnonymousUser
    public void registerUser_Fail() throws Exception {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel()
                .setEmail(encryptionService.encrypt("test@email.bg"))
                .setPassword(encryptionService.encrypt(""))
                .setConfirmPassword(encryptionService.encrypt(""));

        MvcResult result = mockMvc.perform(post("/users/register")
                        .flashAttr("userRegisterBindingModel", userRegisterBindingModel)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonResponse);

        // Extract and assert on errors array
        JsonNode errorsNode = jsonNode.get("errors");
        assertNotNull(errorsNode);
        assertTrue(errorsNode.isArray());

        List<JsonNode> errorsList = new ArrayList<>();
        errorsNode.forEach(errorsList::add);
        errorsList.sort(Comparator.comparing(node -> node.get("code").asText()));

        assertEquals(5, errorsList.size());

        // Example assertions on specific error messages
        assertEquals("You need to tell us what is your name.",
                errorsList.get(0).get("defaultMessage").asText());

        assertEquals("You need to tell us what is your name.",
                errorsList.get(1).get("defaultMessage").asText());

        assertEquals("We need your age to verify that you are over 18 years old.",
                errorsList.get(2).get("defaultMessage").asText());

        assertEquals("Password is empty.",
                errorsList.get(3).get("defaultMessage").asText());

        assertEquals("Confirm your password, please.",
                errorsList.get(4).get("defaultMessage").asText());
    }
}
