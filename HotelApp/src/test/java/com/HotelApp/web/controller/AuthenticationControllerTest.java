package com.HotelApp.web.controller;

import com.HotelApp.service.UserService;
import com.HotelApp.service.impl.AppUserDetailsService;
import com.HotelApp.util.encryptionUtil.EncryptionUtil;
import com.HotelApp.util.filter.DecryptionFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.HotelApp.config.ApplicationSecurityConfiguration.passwordEncoder;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthenticationControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AppUserDetailsService appUserDetailsService;

    private MockMvc mockMvc;
    private SecretKey secretKey;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new DecryptionFilter(appUserDetailsService), "/*")
                .build();

        secretKey = EncryptionUtil.generateKey();
    }

    @Test
    public void testLoginSuccess() throws Exception {
        String username = "testUser";
        String password = "testPass";

        String encryptedUsername = EncryptionUtil.encrypt(username, secretKey);
        String encryptedPassword = EncryptionUtil.encrypt(password, secretKey);
        byte[] ivBytes = new byte[16];
        String iv = Base64.getEncoder().encodeToString(ivBytes);

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("encryptedEmail", encryptedUsername);
        requestParams.put("encryptedPass", encryptedPassword);
        requestParams.put("iv", iv);
        requestParams.put("key", Base64.getEncoder().encodeToString(secretKey.getEncoded()));

        UserDetails userDetails = User.withUsername(password).password(password).authorities("ROLE_USER").build();
        when(appUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        PasswordEncoder passwordEncoderMock;
        try (MockedStatic<PasswordEncoder> mockedPasswordEncoder = mockStatic(PasswordEncoder.class)) {
            passwordEncoderMock = mock(PasswordEncoder.class);
        }

        // Mock PasswordEncoder matches method
        when(passwordEncoderMock.matches(userDetails.getPassword(), password)).thenReturn(true);

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestParams))
                        .requestAttr("LOGIN_ERROR_FLAG", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login success"));
    }

    @Test
    public void testLoginFailure() throws Exception {
        String username = "testUser";
        String password = "wrongPass";

        String encryptedUsername = EncryptionUtil.encrypt(username, secretKey);
        String encryptedPassword = EncryptionUtil.encrypt(password, secretKey);
        byte[] ivBytes = new byte[16];
        String iv = Base64.getEncoder().encodeToString(ivBytes);

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("encryptedEmail", encryptedUsername);
        requestParams.put("encryptedPass", encryptedPassword);
        requestParams.put("iv", iv);
        requestParams.put("key", Base64.getEncoder().encodeToString(secretKey.getEncoded()));

        UserDetails userDetails = User.withUsername(username).password("correctPassword").authorities("ROLE_USER").build();
        given(appUserDetailsService.loadUserByUsername(anyString())).willReturn(userDetails);

        when(appUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        PasswordEncoder passwordEncoderMock;
        try (MockedStatic<PasswordEncoder> mockedPasswordEncoder = mockStatic(PasswordEncoder.class)) {
            passwordEncoderMock = mock(PasswordEncoder.class);
        }

        // Mock PasswordEncoder matches method
        when(passwordEncoderMock.matches(userDetails.getPassword(), password)).thenReturn(false);

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestParams))
                        .requestAttr("LOGIN_ERROR_FLAG", "true"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }
}
