package com.HotelApp.service.impl;

import com.HotelApp.common.constants.ValidationConstants;
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
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.HotelApp.common.constants.AppConstants.*;
import static com.HotelApp.common.constants.BindingConstants.*;
import static com.HotelApp.common.constants.FailConstants.ERROR_MESSAGE;
import static com.HotelApp.common.constants.SuccessConstants.PICTURE_UPLOAD_SUCCESS;
import static com.HotelApp.common.constants.SuccessConstants.SUCCESS_MESSAGE;
import static com.HotelApp.common.constants.ValidationConstants.*;
import static com.HotelApp.config.ApplicationBeanConfiguration.passwordEncoder;
import static com.HotelApp.util.ResponseUtil.genericFailResponse;
import static com.HotelApp.util.ResponseUtil.genericSuccessResponse;


@Service
public class UserServiceImpl implements UserService {
    private static final String REGISTER_SUCCESS_REDIRECT_URL = "/users/registrationSuccess";
    private static final String EDIT_PROFILE_SUCCESS_REDIRECT_URL = "/users/profile/editSuccess";
    private static final String CHANGE_PASSWORD_SUCCESS_REDIRECT_URL = "/users/profile";
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final HotelServiceImpl hotelService;
    private final UserTransformationService userTransformationService;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleService roleService,
            HotelServiceImpl hotelService,
            UserTransformationService userTransformationService
    ) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.hotelService = hotelService;
        this.userTransformationService = userTransformationService;
    }

    @Transactional
    @Override
    public ResponseEntity<?> registerUser(UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult) {
        String decryptedEmail = userTransformationService.decrypt(
                userRegisterBindingModel.getEmail()
        ).toLowerCase().trim();

        String decryptedPass = userTransformationService.decrypt(
                userRegisterBindingModel.getPassword()
        ).trim();

        String decryptedConfirmPass = userTransformationService.decrypt(
                userRegisterBindingModel.getConfirmPassword()
        ).trim();

        if (Objects.requireNonNull(decryptedPass).isEmpty()) {
            bindingResult.addError(new FieldError(USER_REGISTER_BINDING_MODEL,
                    "password", EMPTY_PASSWORD));
        }

        if (Objects.requireNonNull(decryptedConfirmPass).isEmpty()) {
            bindingResult.addError(new FieldError(USER_REGISTER_BINDING_MODEL,
                    "confirmPassword", CONFIRM_PASSWORD));
        }

        if (!decryptedPass.equals(decryptedConfirmPass)) {
            bindingResult.addError(new FieldError(USER_REGISTER_BINDING_MODEL,
                    "confirmPassword", PASSWORD_MISMATCH));
        }

        if (checkIfEmailExist(decryptedEmail)) {
            bindingResult.addError(new FieldError(USER_REGISTER_BINDING_MODEL,
                    EMAIL, ValidationConstants.EMAIL_EXIST));
        }

        if (bindingResult.hasErrors()) {
            return genericFailResponse(bindingResult);
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
        log.info("User {} registered successfully", user.getEmail());
        return genericSuccessResponse(REGISTER_SUCCESS_REDIRECT_URL);
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
            throw new ForbiddenUserException(FORBIDDEN_USER);
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
                throw new ForbiddenUserException(FORBIDDEN_USER);
            }
        } else {
            throw new UsernameNotFoundException(USER_NOT_FOUND + decryptedEmail);
        }

        switch (command) {
            case ADMIN -> user.setRoles(roleService.getAllRoles());
            case MODERATOR -> user.setRoles(roleService.getModeratorRole());
            case USER -> user.setRoles(List.of(roleService.getUserRole()));
        }
        userRepository.save(user);
        userTransformationService.evictUserViewsCache();
    }

    @Override
    public UserView findUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userTransformationService.mapAsUserView(findUser(userEmail));
    }

    @Override
    public String addUserImage(MultipartFile image, RedirectAttributes redirectAttributes) {
        if (image.getSize() > (IMAGE_MAX_SIZE)) {
            throw new MaxUploadSizeExceededException(IMAGE_MAX_SIZE);
        }
        if (image.isEmpty()) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, EMPTY_FILE);
            return "redirect:/users/profile";
        }

        String filename = Objects.requireNonNull(image.getOriginalFilename());
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        if (!isAllowedExtension(extension)) {
            throw new FileNotAllowedException(EXTENSION_NOT_ALLOWED);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        UserEntity user = findUser(userEmail);
        try {
            // Read the image
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
            // Convert image to RGB format if necessary
            BufferedImage rgbImage = new BufferedImage(
                    bufferedImage.getWidth(),
                    bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = rgbImage.createGraphics();
            graphics.drawImage(bufferedImage, 0, 0, null);
            graphics.dispose();
            // Compress the image
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(rgbImage)
                    .size(500, 500)  // Resize the image to a max width/height of 500px
                    .outputQuality(0.5)  // Adjust the quality (1.0 is max quality, 0.3 is 30% quality)
                    .outputFormat(extension)
                    .toOutputStream(outputStream);

            byte[] compressedImageBytes = outputStream.toByteArray();
            Blob blob = new SerialBlob(compressedImageBytes);
            user.setUserImage(blob);

            redirectAttributes.addFlashAttribute(
                    SUCCESS_MESSAGE, PICTURE_UPLOAD_SUCCESS
            );

            userRepository.save(user);

        } catch (SQLException | IOException e) {
            redirectAttributes.addFlashAttribute(
                    ERROR_MESSAGE, FILE_NOT_ALLOWED
            );
        }
        userTransformationService.evictUserViewsCache();
        return "redirect:/users/profile";
    }

    @Override
    public ResponseEntity<?> editProfileInfo(
            EditUserProfileBindingModel editUserProfileBindingModel,
            BindingResult bindingResult
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        String decryptedEmail = userTransformationService.decrypt(
                editUserProfileBindingModel.getEmail()
        );

        if (decryptedEmail.isEmpty()) {
            bindingResult.addError(new FieldError(
                    USER_REGISTER_BINDING_MODEL, EMAIL, EMAIL_NOT_BLANK)
            );
        }

        UserEntity user = findUser(userEmail);
        boolean emailChanged = !user.getEmail().equals(decryptedEmail);

        if (checkIfEmailExist(decryptedEmail) && emailChanged) {
            bindingResult.addError(
                    new FieldError(
                            USER_REGISTER_BINDING_MODEL, EMAIL, EMAIL_EXIST
                    )
            );
        }

        if (bindingResult.hasErrors()) {
            return genericFailResponse(bindingResult);
        }

        user.setFirstName(editUserProfileBindingModel.getFirstName().trim());
        user.setLastName(editUserProfileBindingModel.getLastName().trim());
        user.setEmail(decryptedEmail);
        user.setAge(editUserProfileBindingModel.getAge());

        userRepository.save(user);
        userTransformationService.reAuthenticateUser(decryptedEmail);
        userTransformationService.evictUserViewsCache();

        return genericSuccessResponse(EDIT_PROFILE_SUCCESS_REDIRECT_URL);
    }

    @Override
    public ResponseEntity<?> changeUserPassword(ChangeUserPasswordBindingModel changeUserPasswordBindingModel, BindingResult bindingResult) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity user = findUser(userEmail);

        String decryptedOldPassword = userTransformationService.decrypt(
                changeUserPasswordBindingModel.getOldPassword()
        );
        String decryptedNewPassword = userTransformationService.decrypt(
                changeUserPasswordBindingModel.getNewPassword()
        );
        String decryptedConfirmNewPassword = userTransformationService.decrypt(
                changeUserPasswordBindingModel.getConfirmNewPassword()
        );

        if (!passwordEncoder().matches(decryptedOldPassword, user.getPassword())) {
            bindingResult.addError(
                    new FieldError(
                            CHANGE_PASSWORD_BINDING_MODEL, "oldPassword", OLD_PASSWORD_MISMATCH
                    )
            );
        }
        if (decryptedNewPassword.isEmpty()) {
            bindingResult.addError(
                    new FieldError(
                            CHANGE_PASSWORD_BINDING_MODEL,
                            "newPassword", EMPTY_PASSWORD)
            );
        }
        if (!decryptedNewPassword.equals(decryptedConfirmNewPassword) || decryptedConfirmNewPassword.isEmpty()) {
            bindingResult.addError(
                    new FieldError(CHANGE_PASSWORD_BINDING_MODEL, "confirmNewPassword", CONFIRM_PASSWORD)
            );
        }

        if (bindingResult.hasErrors()) {
            return genericFailResponse(bindingResult);
        }

        user.setPassword(passwordEncoder().encode(decryptedNewPassword));
        userRepository.save(user);

        userTransformationService.reAuthenticateUser(userEmail);
        return genericSuccessResponse(CHANGE_PASSWORD_SUCCESS_REDIRECT_URL);
    }

    private boolean isAllowedExtension(String extension) {
        return extension.equals("jpg") ||
                extension.equals("jpeg") ||
                extension.equals("png") ||
                extension.equals("svg") ||
                extension.equals("gif");
    }

    private UserEntity findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND + email));
    }
}
