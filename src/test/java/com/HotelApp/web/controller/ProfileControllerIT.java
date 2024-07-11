package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.RoleEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.domain.models.binding.ChangeUserPasswordBindingModel;
import com.HotelApp.domain.models.binding.EditUserProfileBindingModel;
import com.HotelApp.domain.models.service.CustomUser;
import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.repository.UserRepository;
import com.HotelApp.service.UserService;
import com.HotelApp.service.impl.AppUserDetailsService;
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

import static com.HotelApp.config.ApplicationSecurityConfiguration.passwordEncoder;
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

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AppUserDetailsService detailsService;

    @Autowired
    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        CustomUser customUser = new CustomUser(
                "user@example.com", "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                "John Doe"
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(detailsService.loadUserByUsername("updated@mail.bg")).thenReturn(customUser);
        when(detailsService.loadUserByUsername("user@example.com")).thenReturn(customUser);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void profile_ShouldReturnProfileView() throws Exception {
        mockMvc.perform(get("/users/profile")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("users/profile"));
    }

    @Test
    void getUserDetails_ShouldReturnUserDetails() throws Exception {
        UserView userView = new UserView();
        userView.setEmail("user@example.com");
        userView.setFirstName("John");
        userView.setLastName("Doe");
        userView.setFullName("John Doe");
        userView.setAge(30);
        userView.setRoles(Collections.singletonList(new RoleEntity(RoleEnum.USER)));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockUserEntity()));

        mockMvc.perform(get("/users/profile/details")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.email").value("user@user.bg"))
                .andExpect(jsonPath("$.firstName").value("User"))
                .andExpect(jsonPath("$.lastName").value("Userov"))
                .andExpect(jsonPath("$.fullName").value("User Userov"))
                .andExpect(jsonPath("$.age").value(33))
                .andExpect(jsonPath("$.roles[0].name").value("USER"));
    }

    @Test
    void checkUserDetailsInSession_ShouldContainExpectedValues() throws Exception {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockUserEntity()));

        MvcResult result = mockMvc.perform(get("/users/profile/details")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String principal = Objects.requireNonNull(result.getRequest().getSession())
                .getAttribute("SPRING_SECURITY_CONTEXT")
                .toString();

        assertTrue(principal.contains("user@example.com"));
        assertTrue(principal.contains("ROLE_USER"));
    }

    @Test
    void editProfile_ShouldReturnBadRequest() throws Exception {
        MvcResult result = mockMvc.perform(post("/users/profile/editUserProfile")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false)).andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract and assert on errors array
        JsonNode errorsNode = jsonNode.get("errors");
        assertNotNull(errorsNode);
        assertTrue(errorsNode.isArray());
        assertEquals(6, errorsNode.size());
    }

    @Test
    public void editProfile_ShouldReturnSuccessResponse_WhenEditIsSuccessful() throws Exception {
        EditUserProfileBindingModel model = new EditUserProfileBindingModel()
                .setFirstName("Test")
                .setLastName("UpdatedLastName")
                .setEmail(encryptionService.encrypt("updated@mail.bg"))
                .setAge(30);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockUserEntity()));

        mockMvc.perform(post("/users/profile/editUserProfile")
                        .flashAttr("editUserProfileBindingModel", model)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.redirectUrl").value("/users/profile/editSuccess"));
    }

    @Test
    public void changePassword__ShouldReturnSuccessResponse_WhenChangePasswordIsSuccessful() throws Exception {
        ChangeUserPasswordBindingModel model = new ChangeUserPasswordBindingModel()
                .setOldPassword(encryptionService.encrypt("testing"))
                .setNewPassword(encryptionService.encrypt("newTest"))
                .setConfirmNewPassword(encryptionService.encrypt("newTest"));

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockUserEntity()));

        mockMvc.perform(post("/users/profile/changePassword")
                        .flashAttr("changeUserPasswordBindingModel", model)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.redirectUrl").value("/users/profile"));
    }

    @Test
    public void changePassword__ShouldReturnFailResponse_WhenChangePasswordIsNotSuccessful() throws Exception {
        ChangeUserPasswordBindingModel model = new ChangeUserPasswordBindingModel()
                .setOldPassword(encryptionService.encrypt(""))
                .setNewPassword(encryptionService.encrypt(""))
                .setConfirmNewPassword(encryptionService.encrypt(""));

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockUserEntity()));

        MvcResult result = mockMvc.perform(post("/users/profile/changePassword")
                        .flashAttr("changeUserPasswordBindingModel", model)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response);

        // Extract and assert on errors array
        JsonNode errorsNode = jsonNode.get("errors");
        assertNotNull(errorsNode);
        assertTrue(errorsNode.isArray());
        assertEquals(3, errorsNode.size());
    }

    private UserEntity mockUserEntity() {
        return new UserEntity()
                .setFirstName("User")
                .setLastName("Userov")
                .setCreated(LocalDateTime.now())
                .setPassword(passwordEncoder().encode("testing"))
                .setAge(33)
                .setEmail("user@user.bg")
                .setRoles(Collections.singletonList(new RoleEntity(RoleEnum.USER)));
    }
}