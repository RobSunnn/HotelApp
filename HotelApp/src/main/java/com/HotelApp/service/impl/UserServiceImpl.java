package com.HotelApp.service.impl;

import com.HotelApp.common.constants.BindingConstants;
import com.HotelApp.common.constants.ValidationConstants;
import com.HotelApp.domain.entity.RoleEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.domain.models.binding.ChangeUserPasswordBindingModel;
import com.HotelApp.domain.models.binding.EditUserProfileBindingModel;
import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.repository.UserRepository;
import com.HotelApp.service.RoleService;
import com.HotelApp.service.UserService;
import com.HotelApp.service.exception.FileNotAllowedException;
import com.HotelApp.service.exception.ForbiddenUserException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.HotelApp.common.constants.BindingConstants.*;
import static com.HotelApp.config.ApplicationSecurityConfiguration.passwordEncoder;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final HotelServiceImpl hotelService;
    private final UserTransformationService userTransformationService;

    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           HotelServiceImpl hotelService,
                           UserTransformationService userTransformationService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.hotelService = hotelService;
        this.userTransformationService = userTransformationService;
    }

    @Transactional
    @Override
    public boolean registerUser(UserRegisterBindingModel userRegisterBindingModel,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        String decryptedEmail = userTransformationService.decrypt(
                userRegisterBindingModel.getEmail(),
                userRegisterBindingModel.getIv(),
                userRegisterBindingModel.getKey()
        );

        String decryptedPass = userTransformationService.decrypt(
                userRegisterBindingModel.getPassword(),
                userRegisterBindingModel.getIv(),
                userRegisterBindingModel.getKey()
        );

        String decryptedConfirmPass = userTransformationService.decrypt(
                userRegisterBindingModel.getConfirmPassword(),
                userRegisterBindingModel.getIv(),
                userRegisterBindingModel.getKey()
        );

        if (Objects.requireNonNull(decryptedPass).isEmpty()) {
            bindingResult.addError(new FieldError("userRegisterBindingModel",
                    "password", "Password is empty."));
        }

        if (Objects.requireNonNull(decryptedConfirmPass).isEmpty()) {
            bindingResult.addError(new FieldError("userRegisterBindingModel",
                    "confirmPassword", "Confirm your password, please."));
        }

        if (!decryptedPass.equals(decryptedConfirmPass)) {
            bindingResult.addError(new FieldError("userRegisterBindingModel",
                    "confirmPassword", "Password mismatch"));
        }
        if (checkIfEmailExist(decryptedEmail)) {
            bindingResult.addError(new FieldError(USER_REGISTER_BINDING_MODEL,
                    EMAIL, ValidationConstants.EMAIL_EXIST));
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute(USER_REGISTER_BINDING_MODEL, userRegisterBindingModel);
            redirectAttributes
                    .addFlashAttribute(BINDING_RESULT_PATH +
                            USER_REGISTER_BINDING_MODEL, bindingResult);
            return false;
        }

        userRegisterBindingModel.setEmail(decryptedEmail);
        userRegisterBindingModel.setPassword(decryptedPass);
        userRegisterBindingModel.setConfirmPassword(decryptedConfirmPass);

        UserEntity user = userTransformationService.mapAsUser(userRegisterBindingModel);
        user.setHotelInfoEntity(hotelService.getHotelInfo());

        if (roleService.getCount() == 0) {
            roleService.initRoles();
        }
        if (userRepository.count() == 0) {
            user.setRoles(roleService.getAllRoles());
        } else {
            user.setRoles(roleService.getAllRoles().stream().filter(r -> r.getName().equals(RoleEnum.USER)).toList());
        }

        userRepository.save(user);
        return user.getEmail() != null;
    }


    @Override
    public boolean checkIfEmailExist(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    @Override
    public UserView findUserByEmail(String email) {
        String decryptedEmail = userTransformationService.decrypt(email);
        UserEntity user = findUser(decryptedEmail);
        if (user.getId() == 1) {
            throw new ForbiddenUserException("You can't see this user :)");
        }
        return userTransformationService.mapAsUserView(user);
    }


    @Override
    public void changeUserRole(String encryptedInfo, String command) {
        String decryptedEmail = userTransformationService.decrypt(encryptedInfo);
        Optional<UserEntity> userOptional = userRepository.findByEmail(decryptedEmail);
        UserEntity user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (user.getId() == 1) {
                throw new ForbiddenUserException("Don't try this.");
            }
        } else {
            throw new UsernameNotFoundException("User not found for email: " + decryptedEmail);
        }

        switch (command) {
            case "Make Admin" -> {
                RoleEntity adminRole = roleService.getAllRoles()
                        .stream()
                        .filter(role -> role.getName().name().equals("ADMIN"))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));
                List<RoleEntity> allRoles = roleService.getAllRoles();
                boolean isAdmin = user.getRoles().stream()
                        .anyMatch(role -> role.getName().name().equals(adminRole.getName().name()));

                if (!isAdmin) {
                    user.setRoles(allRoles);
                    userRepository.save(user);
                } else {
                    System.out.println("User is already an hotel.");
                }
            }
            case "Make Moderator" -> {
                RoleEntity userRole = roleService.getAllRoles()
                        .stream()
                        .filter(role -> role.getName().name().equals("USER"))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("USER role not found"));
                RoleEntity moderatorRole = roleService.getAllRoles()
                        .stream()
                        .filter(role -> role.getName().name().equals("MODERATOR"))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("MODERATOR role not found"));
                boolean isModerator = user.getRoles().stream()
                        .anyMatch(role -> role.getName().name().equals("MODERATOR"));
                user.setRoles(List.of(userRole, moderatorRole));
                userRepository.save(user);
            }
            case "Make User" -> {
                RoleEntity roleUser = roleService.getAllRoles()
                        .stream()
                        .filter(role -> role.getName().name().equals("USER"))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("USER role not found"));
                user.setRoles(List.of(roleUser));
                userRepository.save(user);
            }
        }
    }

    @Override
    public UserView findUserDetails(String userEmail) {
        return userTransformationService.mapAsUserView(findUser(userEmail));
    }

    @Override
    public void addUserImage(MultipartFile image,
                             String userEmail,
                             RedirectAttributes redirectAttributes) {
        if (image.getSize() > (5 * 1024 * 1024)) {
            throw new MaxUploadSizeExceededException(5 * 1024 * 1024);
        }
        if (image.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Please select a file.");
            return;
        }

        String filename = image.getOriginalFilename();
        assert filename != null;

        if (!isAllowedExtension(filename)) {
            throw new FileNotAllowedException("File type not supported.");
        }
        UserEntity user = findUser(userEmail);

        try {
            byte[] imageBytes = image.getBytes();
            Blob blob = new SerialBlob(imageBytes);
            user.setUserImage(blob);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Profile picture uploaded successfully.");

            userRepository.save(user);

        } catch (SQLException | IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public boolean editProfileInfo(EditUserProfileBindingModel editUserProfileBindingModel,
                                   String userEmail,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        UserEntity user = findUser(userEmail);
        boolean emailChanged = !user.getEmail().equals(editUserProfileBindingModel.getEmail());

        if (checkIfEmailExist(editUserProfileBindingModel.getEmail()) && emailChanged) {
            bindingResult.addError(new FieldError("userRegisterBindingModel",
                    "email", ValidationConstants.EMAIL_EXIST));
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("editUserProfileBindingModel", editUserProfileBindingModel);
            redirectAttributes
                    .addFlashAttribute(BindingConstants.BINDING_RESULT_PATH +
                            "editUserProfileBindingModel", bindingResult);
            return false;
        }

        user.setFirstName(editUserProfileBindingModel.getFirstName().trim());
        user.setLastName(editUserProfileBindingModel.getLastName().trim());

        user.setEmail(editUserProfileBindingModel.getEmail().trim());
        user.setAge(editUserProfileBindingModel.getAge());

        userRepository.save(user);

        userTransformationService.authenticateUser(editUserProfileBindingModel.getEmail());
        redirectAttributes.addFlashAttribute("successMessage",
                "Profile info updated successfully.");

        return true;
    }

    @Override
    public boolean changeUserPassword(String userEmail,
                                      ChangeUserPasswordBindingModel changeUserPasswordBindingModel,
                                      BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        UserEntity user = findUser(userEmail);

        if (!passwordEncoder().matches(changeUserPasswordBindingModel.getOldPassword(), user.getPassword())) {
            bindingResult.addError(new FieldError(CHANGE_PASSWORD_BINDING_MODEL,
                    "oldPassword", ValidationConstants.OLD_PASS_MISMATCH));
        }
        if (!changeUserPasswordBindingModel.getNewPassword().equals(changeUserPasswordBindingModel.getConfirmNewPassword())) {
            bindingResult.addError(new FieldError(CHANGE_PASSWORD_BINDING_MODEL,
                    "confirmNewPassword", ValidationConstants.CONFIRM_PASSWORD));
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute(CHANGE_PASSWORD_BINDING_MODEL, changeUserPasswordBindingModel);
            redirectAttributes
                    .addFlashAttribute(BindingConstants.BINDING_RESULT_PATH +
                            CHANGE_PASSWORD_BINDING_MODEL, bindingResult);
            return false;
        }

        user.setPassword(passwordEncoder().encode(changeUserPasswordBindingModel.getNewPassword()));
        userRepository.save(user);

        userTransformationService.authenticateUser(userEmail);
        redirectAttributes.addFlashAttribute("successMessage",
                "Password changed successfully.");

        return true;
    }

    private boolean isAllowedExtension(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        return  extension.equals("jpg") ||
                extension.equals("jpeg") ||
                extension.equals("png") ||
                extension.equals("svg") ||
                extension.equals("avif") ||
                extension.equals("gif");
    }

    private UserEntity findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }
}
