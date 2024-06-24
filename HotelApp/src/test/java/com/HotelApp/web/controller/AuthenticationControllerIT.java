package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.service.UserService;
import com.HotelApp.util.encryptionUtil.EncryptionUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthenticationControllerIT {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    public void testLoginSuccess() throws Exception {
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("LOGIN_ERROR_FLAG", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login success"));
    }

    @Test
    public void testLoginFailure() throws Exception {
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("LOGIN_ERROR_FLAG", "true"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    @WithAnonymousUser
    public void registerUser_Success() throws Exception {
        SecretKey key = EncryptionUtil.generateKey();
        String keyString = EncryptionUtil.keyToString(key);

        byte[] ivBytes = new byte[16];
        String iv = Base64.getEncoder().encodeToString(ivBytes);

        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel()
                .setFirstName("Test")
                .setLastName("User")
                .setEmail(EncryptionUtil.encrypt("testov@email.bg", key, iv))
                .setAge(33)
                .setPassword(EncryptionUtil.encrypt("testing", key, iv))
                .setConfirmPassword(EncryptionUtil.encrypt("testing", key, iv))
                .setKey(keyString)
                .setIv(iv);

        mockMvc.perform(post("/users/register")
                        .flashAttr("userRegisterBindingModel", userRegisterBindingModel)
                        .param("key", keyString)
                        .param("iv", iv))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.redirectUrl", is("/users/registrationSuccess")));

        userRegisterBindingModel
                .setEmail(EncryptionUtil.encrypt("Test@mail.bg", key, iv))
                .setPassword(EncryptionUtil.encrypt("testing", key, iv))
                .setConfirmPassword(EncryptionUtil.encrypt("testing", key, iv));

        assertTrue(userService.registerUser(userRegisterBindingModel, bindingResult, redirectAttributes));
    }

    @Test
    @WithAnonymousUser
    public void registerUser_Fail() throws Exception {
        SecretKey key = EncryptionUtil.generateKey();
        String keyString = EncryptionUtil.keyToString(key);

        byte[] ivBytes = new byte[16];
        String iv = Base64.getEncoder().encodeToString(ivBytes);

        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel()
                .setEmail(EncryptionUtil.encrypt("test@email.bg", key, iv))
                .setPassword(EncryptionUtil.encrypt("", key, iv))
                .setConfirmPassword(EncryptionUtil.encrypt("", key, iv));

        MvcResult result = mockMvc.perform(post("/users/register")
                        .flashAttr("userRegisterBindingModel", userRegisterBindingModel)
                        .param("key", keyString)
                        .param("iv", iv))
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
