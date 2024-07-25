package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.RoleEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.util.encryptionUtil.EncryptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;

import static com.HotelApp.config.ApplicationBeanConfiguration.passwordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTransformationServiceTest {
    @Mock
    private AppUserDetailsService userDetailsService;

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private UserTransformationService userTransformationService;

    @Test
    void transformUsers_ShouldReturnUserViews() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setAge(30);
        userEntity.setRoles(Collections.singletonList(new RoleEntity(RoleEnum.USER)));

        when(encryptionService.encrypt("john.doe@example.com")).thenReturn("encryptedEmail");

        List<UserView> userViews = userTransformationService.transformUsers(Collections.singletonList(userEntity));

        assertNotNull(userViews);
        assertEquals(1, userViews.size());
        UserView userView = userViews.get(0);
        assertEquals("John", userView.getFirstName());
        assertEquals("Doe", userView.getLastName());
        assertEquals("john.doe@example.com", userView.getEmail());
        assertEquals("John Doe", userView.getFullName());
        assertEquals(30, userView.getAge());
        assertEquals("encryptedEmail", userView.getEncryptedEmail());
        assertEquals(1, userView.getRoles().size());
        assertEquals(RoleEnum.USER, userView.getRoles().get(0).getName());
    }


    @Test
    void authenticateUser_ShouldReturnFalse_WhenCredentialsAreIncorrect() throws Exception {
        String encryptedEmail = "encryptedEmail";
        String encryptedPassword = "encryptedPassword";
        String decryptedEmail = "john.doe@example.com";
        String decryptedPassword = "wrongPassword";
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetails.getPassword()).thenReturn(passwordEncoder().encode("password"));

        when(encryptionService.decrypt(encryptedEmail)).thenReturn(decryptedEmail);
        when(encryptionService.decrypt(encryptedPassword)).thenReturn(decryptedPassword);
        when(userDetailsService.loadUserByUsername(decryptedEmail)).thenReturn(userDetails);

        boolean isAuthenticated = userTransformationService.authenticateUser(encryptedEmail, encryptedPassword);
        assertFalse(isAuthenticated);
        // Manually clear the SecurityContextHolder for verification
        SecurityContextHolder.clearContext();
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

}