package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.RoleEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.domain.models.binding.ChangeUserPasswordBindingModel;
import com.HotelApp.domain.models.binding.EditUserProfileBindingModel;
import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.repository.UserRepository;
import com.HotelApp.service.impl.AppUserDetailsService;
import com.HotelApp.service.impl.CustomUser;
import com.HotelApp.util.encryptionUtil.EncryptionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static com.HotelApp.common.constants.AppConstants.ROLE_PREFIX;
import static com.HotelApp.common.constants.AppConstants.SPRING_SECURITY_CONTEXT;
import static com.HotelApp.common.constants.BindingConstants.CHANGE_PASSWORD_BINDING_MODEL;
import static com.HotelApp.common.constants.BindingConstants.EDIT_USER_PROFILE_BINDING_MODEL;
import static com.HotelApp.common.constants.FailConstants.ERRORS;
import static com.HotelApp.config.ApplicationBeanConfiguration.passwordEncoder;
import static com.HotelApp.constants.TestConstants.*;
import static com.HotelApp.constants.urlsAndViewsConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ProfileControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AppUserDetailsService detailsService;

    @Autowired
    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        CustomUser customUser = new CustomUser(
                TEST_EMAIL, TEST_PASSWORD,
                Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX + RoleEnum.USER)),
                USER_FULL_NAME
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(detailsService.loadUserByUsername(TEST_UPDATED_EMAIL)).thenReturn(customUser);
        when(detailsService.loadUserByUsername(TEST_EMAIL)).thenReturn(customUser);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void profile_ShouldReturnProfileView() throws Exception {
        mockMvc.perform(get(USER_PROFILE_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_PROFILE_VIEW));
    }

    @Test
    void getUserDetails_ShouldReturnUserDetails() throws Exception {
        UserView userView = new UserView();
        userView.setEmail(TEST_EMAIL);
        userView.setFirstName(MOCK_FIRST_NAME);
        userView.setLastName(MOCK_LAST_NAME);
        userView.setFullName(USER_FULL_NAME);
        userView.setAge(30);
        userView.setRoles(Collections.singletonList(new RoleEntity(RoleEnum.USER)));
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(mockUserEntity()));

        mockMvc.perform(get(USER_DETAILS_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APP_JSON))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.firstName").value(MOCK_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(MOCK_LAST_NAME))
                .andExpect(jsonPath("$.fullName").value(USER_FULL_NAME))
                .andExpect(jsonPath("$.age").value(33));
    }

    @Test
    void checkUserDetailsInSession_ShouldContainExpectedValues() throws Exception {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(mockUserEntity()));

        MvcResult result = mockMvc.perform(get(USER_DETAILS_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String principal = Objects.requireNonNull(result.getRequest().getSession())
                .getAttribute(SPRING_SECURITY_CONTEXT)
                .toString();

        assertTrue(principal.contains(TEST_EMAIL));
        assertTrue(principal.contains(ROLE_PREFIX + RoleEnum.USER));
    }

    @Test
    void editProfile_ShouldReturnBadRequest() throws Exception {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(mockUserEntity()));
        MvcResult result = mockMvc.perform(post(EDIT_USER_PROFILE_URL)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false)).andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract and assert on an error array
        JsonNode errorsNode = jsonNode.get(ERRORS);
        assertNotNull(errorsNode);
        assertTrue(errorsNode.isArray());
        assertEquals(5, errorsNode.size());
    }

    @Test
    void editProfile_ShouldReturnSuccessResponse_WhenEditIsSuccessful() throws Exception {
        EditUserProfileBindingModel model = new EditUserProfileBindingModel()
                .setFirstName(MOCK_FIRST_NAME)
                .setLastName(MOCK_UPDATED_LAST_NAME)
                .setEmail(encryptionService.encrypt(TEST_UPDATED_EMAIL))
                .setAge(30);

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(mockUserEntity()));

        mockMvc.perform(post(EDIT_USER_PROFILE_URL)
                        .flashAttr(EDIT_USER_PROFILE_BINDING_MODEL, model)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.redirectUrl").value(EDIT_USER_PROFILE_SUCCESS_URL));
    }

    @Test
    void changePassword__ShouldReturnSuccessResponse_WhenChangePasswordIsSuccessful() throws Exception {
        ChangeUserPasswordBindingModel model = new ChangeUserPasswordBindingModel()
                .setOldPassword(encryptionService.encrypt(TEST_PASSWORD))
                .setNewPassword(encryptionService.encrypt(TEST_NEW_PASSWORD))
                .setConfirmNewPassword(encryptionService.encrypt(TEST_NEW_PASSWORD));

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(mockUserEntity()));

        mockMvc.perform(post(CHANGE_USER_PASSWORD_URL)
                        .flashAttr(CHANGE_PASSWORD_BINDING_MODEL, model)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.redirectUrl").value(USER_PROFILE_URL));
    }

    @Test
    void changePassword__ShouldReturnFailResponse_WhenChangePasswordIsNotSuccessful() throws Exception {
        ChangeUserPasswordBindingModel model = new ChangeUserPasswordBindingModel()
                .setOldPassword(encryptionService.encrypt(""))
                .setNewPassword(encryptionService.encrypt(""))
                .setConfirmNewPassword(encryptionService.encrypt(""));

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(mockUserEntity()));

        MvcResult result = mockMvc.perform(post(CHANGE_USER_PASSWORD_URL)
                        .flashAttr(CHANGE_PASSWORD_BINDING_MODEL, model)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract and assert on an error array
        JsonNode errorsNode = jsonNode.get(ERRORS);
        assertNotNull(errorsNode);
        assertTrue(errorsNode.isArray());
        assertEquals(3, errorsNode.size());
    }

    private UserEntity mockUserEntity() {
        return new UserEntity()
                .setFirstName(MOCK_FIRST_NAME)
                .setLastName(MOCK_LAST_NAME)
                .setCreated(LocalDateTime.now())
                .setPassword(passwordEncoder().encode(TEST_PASSWORD))
                .setAge(33)
                .setEmail(TEST_EMAIL)
                .setRoles(Collections.singletonList(new RoleEntity(RoleEnum.USER)));
    }
}