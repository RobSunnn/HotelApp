package com.HotelApp.service.impl;

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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.HotelApp.common.constants.FailConstants.ERROR_MESSAGE;
import static com.HotelApp.common.constants.SuccessConstants.PICTURE_UPLOAD_SUCCESS;
import static com.HotelApp.common.constants.SuccessConstants.SUCCESS_MESSAGE;
import static com.HotelApp.common.constants.ValidationConstants.*;
import static com.HotelApp.config.ApplicationBeanConfiguration.passwordEncoder;
import static com.HotelApp.service.constants.TestConstants.*;
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

    @BeforeEach
    void setUp() {
        CustomUser customUser = new CustomUser(
                TEST_EMAIL, TEST_PASSWORD,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                USER_FULL_NAME
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testAddUserImage_MaxUploadSizeExceededException() {
        when(image.getSize()).thenReturn(6 * 1024 * 1024L); // 6MB
        assertThrows(MaxUploadSizeExceededException.class, () -> userService.addUserImage(image, redirectAttributes));
    }

    @Test
    public void testAddUserImage_EmptyFile() {
        when(image.getSize()).thenReturn(0L);
        when(image.isEmpty()).thenReturn(true);

        userService.addUserImage(image, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute(ERROR_MESSAGE, EMPTY_FILE);
    }

    @Test
    public void testAddUserImage_FileNotAllowedException() {
        when(image.getSize()).thenReturn(1024L);
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn("file.exe");

        assertThrows(FileNotAllowedException.class, () -> userService.addUserImage(image, redirectAttributes));
    }

    @Test
    public void testAddUserImage_Success() throws Exception {
        when(image.getSize()).thenReturn(1024L);
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn(FILE_JPG);
        when(image.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUserEntity());
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(mockUserEntity()));

        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        when(image.getInputStream()).thenReturn(new ByteArrayInputStream(imageBytes));
        userService.addUserImage(image, redirectAttributes);

        verify(userRepository).save(any(UserEntity.class));
        verify(redirectAttributes).addFlashAttribute(SUCCESS_MESSAGE, PICTURE_UPLOAD_SUCCESS);
        verify(userTransformationService).evictUserViewsCache();
    }

    @Test
    void testChangeUserRoleToAdmin() {
        String encryptedInfo = ADMIN;
        String decryptedEmail = TEST_EMAIL;
        UserEntity user = new UserEntity();
        user.setId(2L);
        user.setEmail(decryptedEmail);

        when(userTransformationService.decrypt(encryptedInfo)).thenReturn(decryptedEmail);
        when(userRepository.findByEmail(decryptedEmail)).thenReturn(Optional.of(user));
        when(roleService.getAllRoles()).thenReturn(List.of(new RoleEntity(RoleEnum.ADMIN), new RoleEntity(RoleEnum.USER)));

        userService.changeUserRole(encryptedInfo, ADMIN);

        verify(userRepository).save(user);
        verify(userTransformationService).evictUserViewsCache();
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().stream().anyMatch(role -> RoleEnum.ADMIN.equals(role.getName())));
    }

    @Test
    void testChangeUserRoleToModerator() {
        String encryptedInfo = MODERATOR;
        String decryptedEmail = TEST_EMAIL;
        UserEntity user = new UserEntity();
        user.setId(2L);
        user.setEmail(decryptedEmail);

        when(userTransformationService.decrypt(encryptedInfo)).thenReturn(decryptedEmail);
        when(userRepository.findByEmail(decryptedEmail)).thenReturn(Optional.of(user));
        when(roleService.getModeratorRole()).thenReturn(List.of(new RoleEntity(RoleEnum.MODERATOR)));

        userService.changeUserRole(encryptedInfo, MODERATOR);

        verify(userRepository).save(user);
        verify(userTransformationService).evictUserViewsCache();
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().stream().anyMatch(role -> RoleEnum.MODERATOR.equals(role.getName())));
    }

    @Test
    void testChangeUserRoleToUser() {
        String encryptedInfo = USER;
        String decryptedEmail = TEST_EMAIL;
        UserEntity user = new UserEntity();
        user.setId(2L);
        user.setEmail(decryptedEmail);

        when(userTransformationService.decrypt(encryptedInfo)).thenReturn(decryptedEmail);
        when(userRepository.findByEmail(decryptedEmail)).thenReturn(Optional.of(user));
        when(roleService.getUserRole()).thenReturn(new RoleEntity(RoleEnum.USER));

        userService.changeUserRole(encryptedInfo, USER);

        verify(userRepository).save(user);
        verify(userTransformationService).evictUserViewsCache();
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().stream().anyMatch(role -> RoleEnum.USER.equals(role.getName())));
    }

    @Test
    void testChangeUserRoleThrowsForbiddenUserException() {
        String encryptedInfo = ADMIN;
        String decryptedEmail = TEST_EMAIL;
        UserEntity user = new UserEntity();
        user.setId(1L); // Admin user

        when(userTransformationService.decrypt(encryptedInfo)).thenReturn(decryptedEmail);
        when(userRepository.findByEmail(decryptedEmail)).thenReturn(Optional.of(user));

        ForbiddenUserException exception = assertThrows(ForbiddenUserException.class, () ->
                userService.changeUserRole(encryptedInfo, ADMIN));

        assertEquals(FORBIDDEN_USER, exception.getMessage());
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(userTransformationService, never()).evictUserViewsCache();
    }

    @Test
    public void testAddUserImage_ExceptionHandling() throws Exception {
        when(image.getSize()).thenReturn(1024L);
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn(FILE_JPG);
        when(image.getInputStream()).thenThrow(new IOException(TEST_EXCEPTION));
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(mockUserEntity()));

        userService.addUserImage(image, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute(ERROR_MESSAGE, FILE_NOT_ALLOWED);
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