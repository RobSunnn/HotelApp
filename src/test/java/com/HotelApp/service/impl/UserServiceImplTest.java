package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.RoleEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.repository.UserRepository;
import com.HotelApp.service.RoleService;
import com.HotelApp.service.exception.FileNotAllowedException;
import com.HotelApp.service.exception.ForbiddenUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.HotelApp.config.ApplicationBeanConfiguration.passwordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTransformationService userTransformationService;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private MultipartFile image;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private RoleService roleService;

    private static final String USER_EMAIL = "user@test.bg";


    @BeforeEach
    void setUp() {
        CustomUser customUser = new CustomUser(
                "user@test.bg", "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                "User Full Name"
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testAddUserImage_MaxUploadSizeExceededException() {
        when(image.getSize()).thenReturn(6 * 1024 * 1024L); // 6MB

        assertThrows(MaxUploadSizeExceededException.class, () -> {
            userService.addUserImage(image, redirectAttributes);
        });
    }

    @Test
    public void testAddUserImage_EmptyFile() {
        when(image.getSize()).thenReturn(0L);
        when(image.isEmpty()).thenReturn(true);

        userService.addUserImage(image, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("errorMessage", "Please select a file.");
    }

    @Test
    public void testAddUserImage_FileNotAllowedException() {
        when(image.getSize()).thenReturn(1024L);
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn("file.exe");

        assertThrows(FileNotAllowedException.class, () -> {
            userService.addUserImage(image, redirectAttributes);
        });
    }

    @Test
    public void testAddUserImage_Success() throws Exception {
        when(image.getSize()).thenReturn(1024L);
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn("file.jpg");
        when(image.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUserEntity());
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUserEntity()));

        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        when(image.getInputStream()).thenReturn(new ByteArrayInputStream(imageBytes));
        userService.addUserImage(image, redirectAttributes);

        verify(userRepository).save(any(UserEntity.class));
        verify(redirectAttributes).addFlashAttribute("successMessage", "Profile picture uploaded successfully.");
        verify(userTransformationService).evictUserViewsCache();
    }

    @Test
    void testChangeUserRoleToAdmin() {
        String encryptedInfo = "encryptedInfo";
        String decryptedEmail = "test@example.com";
        UserEntity user = new UserEntity();
        user.setId(2L);
        user.setEmail(decryptedEmail);

        when(userTransformationService.decrypt(encryptedInfo)).thenReturn(decryptedEmail);
        when(userRepository.findByEmail(decryptedEmail)).thenReturn(Optional.of(user));
        when(roleService.getAllRoles()).thenReturn(List.of(new RoleEntity(RoleEnum.ADMIN), new RoleEntity(RoleEnum.USER)));

        userService.changeUserRole(encryptedInfo, "Admin");

        verify(userRepository).save(user);
        verify(userTransformationService).evictUserViewsCache();
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().stream().anyMatch(role -> RoleEnum.ADMIN.equals(role.getName())));
    }

    @Test
    void testChangeUserRoleToModerator() {
        String encryptedInfo = "encryptedInfo";
        String decryptedEmail = "test@example.com";
        UserEntity user = new UserEntity();
        user.setId(2L);
        user.setEmail(decryptedEmail);

        when(userTransformationService.decrypt(encryptedInfo)).thenReturn(decryptedEmail);
        when(userRepository.findByEmail(decryptedEmail)).thenReturn(Optional.of(user));
        when(roleService.getModeratorRole()).thenReturn(List.of(new RoleEntity(RoleEnum.MODERATOR)));

        userService.changeUserRole(encryptedInfo, "Moderator");

        verify(userRepository).save(user);
        verify(userTransformationService).evictUserViewsCache();
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().stream().anyMatch(role -> RoleEnum.MODERATOR.equals(role.getName())));
    }

    @Test
    void testChangeUserRoleToUser() {
        String encryptedInfo = "encryptedInfo";
        String decryptedEmail = "test@example.com";
        UserEntity user = new UserEntity();
        user.setId(2L);
        user.setEmail(decryptedEmail);

        when(userTransformationService.decrypt(encryptedInfo)).thenReturn(decryptedEmail);
        when(userRepository.findByEmail(decryptedEmail)).thenReturn(Optional.of(user));
        when(roleService.getUserRole()).thenReturn(new RoleEntity(RoleEnum.USER));

        userService.changeUserRole(encryptedInfo, "User");

        verify(userRepository).save(user);
        verify(userTransformationService).evictUserViewsCache();
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().stream().anyMatch(role -> RoleEnum.USER.equals(role.getName())));
    }

    @Test
    void testChangeUserRoleThrowsForbiddenUserException() {
        String encryptedInfo = "encryptedInfo";
        String decryptedEmail = "admin@example.com";
        UserEntity user = new UserEntity();
        user.setId(1L); // Admin user

        when(userTransformationService.decrypt(encryptedInfo)).thenReturn(decryptedEmail);
        when(userRepository.findByEmail(decryptedEmail)).thenReturn(Optional.of(user));

        ForbiddenUserException exception = assertThrows(ForbiddenUserException.class, () ->
                userService.changeUserRole(encryptedInfo, "Admin"));

        assertEquals("Don't try this.", exception.getMessage());
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(userTransformationService, never()).evictUserViewsCache();
    }

    @Test
    public void testAddUserImage_ExceptionHandling() throws Exception {
        when(image.getSize()).thenReturn(1024L);
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn("file.jpg");
        when(image.getInputStream()).thenThrow(new IOException("Test Exception"));
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUserEntity()));

        userService.addUserImage(image, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("errorMessage", "Something went wrong. Please choose different file.");
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