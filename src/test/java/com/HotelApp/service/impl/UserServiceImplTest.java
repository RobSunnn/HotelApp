package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.RoleEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.repository.UserRepository;
import com.HotelApp.service.exception.FileNotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.Optional;

import static com.HotelApp.config.ApplicationSecurityConfiguration.passwordEncoder;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private static final String USER_EMAIL = "user@example.com";
    private UserEntity user;

    @BeforeEach
    public void setUp() {
        user = new UserEntity();
        user.setEmail(USER_EMAIL);
    }

    @Test
    public void testAddUserImage_MaxUploadSizeExceededException() {
        when(image.getSize()).thenReturn(6 * 1024 * 1024L); // 6MB

        assertThrows(MaxUploadSizeExceededException.class, () -> {
            userService.addUserImage(image, USER_EMAIL, redirectAttributes);
        });
    }

    @Test
    public void testAddUserImage_EmptyFile() {
        when(image.getSize()).thenReturn(0L);
        when(image.isEmpty()).thenReturn(true);

        userService.addUserImage(image, USER_EMAIL, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("errorMessage", "Please select a file.");
    }

    @Test
    public void testAddUserImage_FileNotAllowedException() {
        when(image.getSize()).thenReturn(1024L);
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn("file.exe");

        assertThrows(FileNotAllowedException.class, () -> {
            userService.addUserImage(image, USER_EMAIL, redirectAttributes);
        });
    }

    @Test
    public void testAddUserImage_Success() throws Exception {
        when(image.getSize()).thenReturn(1024L);
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn("file.jpg");
        when(image.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUserEntity()));
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUserEntity());

        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        when(image.getInputStream()).thenReturn(new ByteArrayInputStream(imageBytes));
        userService.addUserImage(image, USER_EMAIL, redirectAttributes);

        verify(userRepository).save(any(UserEntity.class));
        verify(redirectAttributes).addFlashAttribute("successMessage", "Profile picture uploaded successfully.");
        verify(userTransformationService).evictUserViewsCache();
    }


    @Test
    public void testAddUserImage_ExceptionHandling() throws Exception {
        when(image.getSize()).thenReturn(1024L);
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn("file.jpg");
        when(image.getInputStream()).thenThrow(new IOException("Test Exception"));
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUserEntity()));

        userService.addUserImage(image, USER_EMAIL, redirectAttributes);

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