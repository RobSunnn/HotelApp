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
import static com.HotelApp.constants.TestConstants.*;
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
        userEntity.setFirstName(MOCK_FIRST_NAME);
        userEntity.setLastName(MOCK_LAST_NAME);
        userEntity.setEmail(TEST_EMAIL);
        userEntity.setAge(30);
        userEntity.setRoles(Collections.singletonList(new RoleEntity(RoleEnum.USER)));

        when(encryptionService.encrypt(TEST_EMAIL)).thenReturn(TEST_EMAIL);

        List<UserView> userViews = userTransformationService.transformUsers(Collections.singletonList(userEntity));

        assertNotNull(userViews);
        assertEquals(1, userViews.size());
        UserView userView = userViews.get(0);
        assertEquals(MOCK_FIRST_NAME, userView.getFirstName());
        assertEquals(MOCK_LAST_NAME, userView.getLastName());
        assertEquals(TEST_EMAIL, userView.getEmail());
        assertEquals(USER_FULL_NAME, userView.getFullName());
        assertEquals(30, userView.getAge());
        assertEquals(TEST_EMAIL, userView.getEncryptedEmail());
        assertEquals(1, userView.getRoles().size());
        assertEquals(RoleEnum.USER, userView.getRoles().get(0).getName());
    }


    @Test
    void authenticateUser_ShouldReturnFalse_WhenCredentialsAreIncorrect() throws Exception {
        String encryptedEmail = TEST_EMAIL;
        String encryptedPassword = TEST_PASSWORD;
        String decryptedEmail = TEST_EMAIL;
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetails.getPassword()).thenReturn(passwordEncoder().encode(TEST_PASSWORD));

        when(encryptionService.decrypt(encryptedEmail)).thenReturn(decryptedEmail);
        when(encryptionService.decrypt(encryptedPassword)).thenReturn(TEST_WRONG_PASSWORD);
        when(userDetailsService.loadUserByUsername(decryptedEmail)).thenReturn(userDetails);

        boolean isAuthenticated = userTransformationService.authenticateUser(encryptedEmail, encryptedPassword);
        assertFalse(isAuthenticated);
        // Manually clear the SecurityContextHolder for verification
        SecurityContextHolder.clearContext();
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

}