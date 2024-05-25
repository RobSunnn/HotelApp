package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.RoleEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.repository.RoleRepository;
import com.HotelApp.repository.UserRepository;
import com.HotelApp.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService; // replace with your service class name

    @InjectMocks
    private RoleService roleService;

    private UserRegisterBindingModel userRegisterBindingModel;
    private HotelInfoEntity hotelInfo;

    @BeforeEach
    void setUp() {
        userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setEmail("test@example.com");
        userRegisterBindingModel.setAge(25);
        userRegisterBindingModel.setFirstName("John");
        userRegisterBindingModel.setLastName("Doe");
        userRegisterBindingModel.setPassword("password");
        userRegisterBindingModel.setConfirmPassword("password");

        hotelInfo = new HotelInfoEntity();
    }

    @Test
    void registerUser_success() {
//        when(userService.checkIfEmailExist(userRegisterBindingModel)).thenReturn(false);

        when(roleRepository.count()).thenReturn(1L);

        RoleEntity userRole = new RoleEntity();
        userRole.setName(RoleEnum.USER);

        when(roleService.getAllRoles()).thenReturn(List.of(userRole));


        when(userRepository.count()).thenReturn(1L);
        // Mock the behavior of userRepository.save method
        UserEntity userEntity = new UserEntity()
        .setEmail(userRegisterBindingModel.getEmail())
                .setAge(userRegisterBindingModel.getAge())
                .setFirstName(userRegisterBindingModel.getFirstName())
                .setLastName(userRegisterBindingModel.getLastName())
                .setPassword("encodedPassword")
                .setCreated(LocalDateTime.now())
                .setHotelInfoEntity(hotelInfo)
                .setRoles(List.of(userRole));
//        userEntity.setEmail("test@example.com");

//        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Call the method
        boolean result = userService.registerUser(userRegisterBindingModel, bindingResult, hotelInfo);

        // Assert that the result is true
        assertTrue(result);

        // Verify that the user was added to the hotel's users
        assertTrue(hotelInfo.getUsers().contains(userEntity));
    }

    @Test
    void checkIfEmailExist() {
        boolean result = userService.registerUser(userRegisterBindingModel, bindingResult, hotelInfo);

        // Assert that the result is false
        assertFalse(result);

        // Verify that an error was added to the BindingResult
        verify(bindingResult).addError(any(FieldError.class));
    }

    @Test
    void findUserByEmail() {
    }

    @Test
    void makeUserAdmin() {
    }

    @Test
    void makeUserModerator() {
    }

    @Test
    void takeRights() {
    }

    @Test
    void findUserDetails() {
    }
}