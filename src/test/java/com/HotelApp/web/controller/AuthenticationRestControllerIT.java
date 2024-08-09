package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.service.impl.AppUserDetailsService;
import com.HotelApp.service.impl.CustomUser;
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

import static com.HotelApp.common.constants.AppConstants.ROLE_PREFIX;
import static com.HotelApp.common.constants.BindingConstants.BAD_CREDENTIALS;
import static com.HotelApp.common.constants.BindingConstants.USER_REGISTER_BINDING_MODEL;
import static com.HotelApp.common.constants.FailConstants.ERRORS;
import static com.HotelApp.common.constants.SuccessConstants.LOGIN_SUCCESS;
import static com.HotelApp.common.constants.ValidationConstants.*;
import static com.HotelApp.config.ApplicationBeanConfiguration.passwordEncoder;
import static com.HotelApp.constants.FieldConstants.ENCRYPTED_EMAIL_FIELD;
import static com.HotelApp.constants.FieldConstants.ENCRYPTED_PASSWORD_FIELD;
import static com.HotelApp.constants.TestConstants.*;
import static com.HotelApp.constants.urlsAndViewsConstants.*;
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
    void testLoginInvalidCredentials() throws Exception {
        mockMvc.perform(post(USER_LOGIN_URL)
                        .param(ENCRYPTED_EMAIL_FIELD, encryptionService.encrypt(TEST_EMAIL))
                        .param(ENCRYPTED_PASSWORD_FIELD, encryptionService.encrypt(TEST_PASSWORD))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BAD_CREDENTIALS));
    }

    @Test
    @WithAnonymousUser
    void testLogin_Success() throws Exception {
        when(userDetailsService.loadUserByUsername(TEST_EMAIL)).thenReturn(
                new CustomUser(
                        TEST_EMAIL,
                        passwordEncoder().encode(TEST_PASSWORD),
                        Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX + RoleEnum.USER)),
                        USER_FULL_NAME
                )
        );
        mockMvc.perform(post(USER_LOGIN_URL)
                        .param(ENCRYPTED_EMAIL_FIELD, encryptionService.encrypt(TEST_EMAIL))
                        .param(ENCRYPTED_PASSWORD_FIELD, encryptionService.encrypt(TEST_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(LOGIN_SUCCESS));
    }

    @Test
    @WithAnonymousUser
    void registerUser_Success() throws Exception {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel()
                .setFirstName(MOCK_FIRST_NAME)
                .setLastName(MOCK_LAST_NAME)
                .setEmail(encryptionService.encrypt(TEST_EMAIL))
                .setAge(33)
                .setPassword(encryptionService.encrypt(TEST_PASSWORD))
                .setConfirmPassword(encryptionService.encrypt(TEST_PASSWORD));


        mockMvc.perform(post(USER_REGISTER_URL)
                        .flashAttr(USER_REGISTER_BINDING_MODEL, userRegisterBindingModel)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.redirectUrl", is(USER_REGISTER_SUCCESS_URL)));
    }

    @Test
    @WithAnonymousUser
    void registerUser_Fail() throws Exception {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel()
                .setEmail(encryptionService.encrypt(TEST_EMAIL))
                .setPassword(encryptionService.encrypt(""))
                .setConfirmPassword(encryptionService.encrypt(""));

        MvcResult result = mockMvc.perform(post(USER_REGISTER_URL)
                        .flashAttr(USER_REGISTER_BINDING_MODEL, userRegisterBindingModel)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
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
        errorsList.sort(Comparator.comparing(node -> node.get(CODE).asText()));

        assertEquals(6, errorsList.size());
        assertEquals(NAME_BLANK, errorsList.get(0).get(DEFAULT_MESSAGE).asText());
        assertEquals(NAME_BLANK, errorsList.get(1).get(DEFAULT_MESSAGE).asText());
        assertEquals(INVALID_AGE, errorsList.get(2).get(DEFAULT_MESSAGE).asText());
        assertEquals(EMPTY_PASSWORD, errorsList.get(3).get(DEFAULT_MESSAGE).asText());
        assertEquals(CONFIRM_PASSWORD, errorsList.get(4).get(DEFAULT_MESSAGE).asText());
        assertEquals(EMAIL_EXIST, errorsList.get(5).get(DEFAULT_MESSAGE).asText());
    }
}
