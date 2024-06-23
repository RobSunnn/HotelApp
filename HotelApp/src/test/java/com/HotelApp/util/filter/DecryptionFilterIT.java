package com.HotelApp.util.filter;

import com.HotelApp.service.impl.AppUserDetailsService;
import com.HotelApp.util.encryptionUtil.EncryptionUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

import java.util.Base64;

import static com.HotelApp.config.ApplicationSecurityConfiguration.passwordEncoder;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class DecryptionFilterIT {

    @Autowired
    private DecryptionFilter decryptionFilter;

    @MockBean
    private AppUserDetailsService appUserDetailsService;

    @Mock
    private ServletRequest servletRequest;

    @Mock
    private ServletResponse servletResponse;

    @Mock
    private FilterChain filterChain;

    @Test
    public void testDecryptionFilter_Success() throws Exception {
        // Given
        String originalUsername = "testuser";
        String originalPassword = "testpassword";

        // Simulate encryption from frontend (JavaScript)
        SecretKey key = EncryptionUtil.generateKey();
        String keyString = EncryptionUtil.keyToString(key);
        byte[] ivBytes = new byte[16];

        String iv = Base64.getEncoder().encodeToString(ivBytes);

        String encryptedUsername = EncryptionUtil.encrypt(originalUsername, key, iv);
        String encryptedPassword = EncryptionUtil.encrypt(originalPassword, key, iv);

        UserDetails userDetails = User.withUsername(originalUsername).password(originalPassword).authorities("ROLE_USER").build();
        when(appUserDetailsService.loadUserByUsername(originalUsername)).thenReturn(userDetails);

        PasswordEncoder passwordEncoderMock;
        try (MockedStatic<PasswordEncoder> mockedPasswordEncoder = mockStatic(PasswordEncoder.class)) {
             passwordEncoderMock = mock(PasswordEncoder.class);
        }

        // Mock PasswordEncoder matches method
        when(passwordEncoderMock.matches(userDetails.getPassword(), originalPassword)).thenReturn(true);

        // Mock servlet request parameters
        when(servletRequest.getParameter("encryptedEmail")).thenReturn(encryptedUsername);
        when(servletRequest.getParameter("encryptedPass")).thenReturn(encryptedPassword);
        when(servletRequest.getParameter("iv")).thenReturn(iv);
        when(servletRequest.getParameter("key")).thenReturn(keyString);

        // When
        decryptionFilter.doFilter(servletRequest, servletResponse, filterChain);

        // Then
        verify(servletRequest, times(2)).setAttribute("LOGIN_ERROR_FLAG", "true");
        verify(filterChain).doFilter(any(), any());

    }


    @Test
    public void testDecryptionFilter_DecryptionFailure() throws Exception {
        // Given
        String encryptedUsername = "invalidEncryptedUsername";
        String encryptedPassword = "invalidEncryptedPassword";
        String iv = "invalidIv";
        String key = "invalidKey";

        // Mock servlet request parameters
        when(servletRequest.getParameter("encryptedEmail")).thenReturn(encryptedUsername);
        when(servletRequest.getParameter("encryptedPass")).thenReturn(encryptedPassword);
        when(servletRequest.getParameter("iv")).thenReturn(iv);
        when(servletRequest.getParameter("key")).thenReturn(key);

        // When
        decryptionFilter.doFilter(servletRequest, servletResponse, filterChain);

        // Then
        verify(servletRequest, times(1)).setAttribute("LOGIN_ERROR_FLAG", "true");
        verify(filterChain).doFilter(any(), any());
    }

    @Test
    public void testDecryptionFilter_UserNotFound() throws Exception {
        // Given
        String originalUsername = "testuser";
        String originalPassword = "testpassword";

        // Simulate encryption from frontend (JavaScript)
        SecretKey key = EncryptionUtil.generateKey();
        String keyString = EncryptionUtil.keyToString(key);
        byte[] ivBytes = new byte[16];

        String iv = Base64.getEncoder().encodeToString(ivBytes);

        String encryptedUsername = EncryptionUtil.encrypt(originalUsername, key, iv);
        String encryptedPassword = EncryptionUtil.encrypt(originalPassword, key, iv);

        // Mock behavior for UserDetailsService to return null (user not found)
        when(appUserDetailsService.loadUserByUsername(originalUsername)).thenReturn(null);

        // Mock servlet request parameters
        when(servletRequest.getParameter("encryptedEmail")).thenReturn(encryptedUsername);
        when(servletRequest.getParameter("encryptedPass")).thenReturn(encryptedPassword);
        when(servletRequest.getParameter("iv")).thenReturn(iv);
        when(servletRequest.getParameter("key")).thenReturn(keyString);

        // When
        decryptionFilter.doFilter(servletRequest, servletResponse, filterChain);

        // Then
        verify(servletRequest, times(2)).setAttribute("LOGIN_ERROR_FLAG", "true");
        verify(filterChain).doFilter(any(), any());
    }

    @Test
    public void testDecryptionFilter_PasswordMismatch() throws Exception {
        // Given
        String originalUsername = "testuser";
        String originalPassword = "testpassword";
        String incorrectPassword = "incorrectpassword";

        // Simulate encryption from frontend (JavaScript)
        SecretKey key = EncryptionUtil.generateKey();
        String keyString = EncryptionUtil.keyToString(key);
        byte[] ivBytes = new byte[16];

        String iv = Base64.getEncoder().encodeToString(ivBytes);

        String encryptedUsername = EncryptionUtil.encrypt(originalUsername, key, iv);
        String encryptedPassword = EncryptionUtil.encrypt(incorrectPassword, key, iv);

        // Mock behavior for UserDetailsService and PasswordEncoder
        UserDetails userDetails = mock(UserDetails.class);
        when(appUserDetailsService.loadUserByUsername(originalUsername)).thenReturn(userDetails);
        // Mock servlet request parameters
        when(servletRequest.getParameter("encryptedEmail")).thenReturn(encryptedUsername);
        when(servletRequest.getParameter("encryptedPass")).thenReturn(encryptedPassword);
        when(servletRequest.getParameter("iv")).thenReturn(iv);
        when(servletRequest.getParameter("key")).thenReturn(keyString);

        // When
        decryptionFilter.doFilter(servletRequest, servletResponse, filterChain);

        // Then
        verify(servletRequest, times(2)).setAttribute("LOGIN_ERROR_FLAG", "true");
        verify(filterChain).doFilter(any(), any());
    }

}