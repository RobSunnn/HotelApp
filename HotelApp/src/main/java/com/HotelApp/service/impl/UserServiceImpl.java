package com.HotelApp.service.impl;

import com.HotelApp.common.constants.BindingConstants;
import com.HotelApp.domain.entity.RoleEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.domain.models.binding.ChangeUserPasswordBindingModel;
import com.HotelApp.domain.models.binding.EditUserProfileBindingModel;
import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.domain.models.service.CustomUserDetails;
import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.repository.UserRepository;
import com.HotelApp.service.RoleService;
import com.HotelApp.service.UserService;
import com.HotelApp.service.exception.FileNotAllowedException;
import com.HotelApp.service.exception.ForbiddenUserException;
import com.HotelApp.service.exception.UserNotFoundException;
import com.HotelApp.common.constants.ValidationConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.HotelApp.common.constants.BindingConstants.*;
import static com.HotelApp.config.ApplicationBeanConfiguration.modelMapper;
import static com.HotelApp.config.ApplicationSecurityConfiguration.passwordEncoder;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final HotelServiceImpl hotelService;
    private final UserDetailsService userDetailsService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;


    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           HotelServiceImpl hotelService,
                           UserDetailsService userDetailsService,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.hotelService = hotelService;
        this.userDetailsService = userDetailsService;
        this.request = request;
        this.response = response;

    }

    @Transactional
    @Override
    public boolean registerUser(UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (checkIfEmailExist(userRegisterBindingModel.getEmail())) {
            bindingResult.addError(new FieldError("userRegisterBindingModel",
                    "email", ValidationConstants.EMAIL_EXIST));
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute(USER_REGISTER_BINDING_MODEL, userRegisterBindingModel);
            redirectAttributes
                    .addFlashAttribute(BINDING_RESULT_PATH +
                            USER_REGISTER_BINDING_MODEL, bindingResult);
            return false;
        }

        UserEntity user = mapAsUser(userRegisterBindingModel);
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
        UserEntity user = findUser(email);

        if (user.getId() == 1) {
            throw new ForbiddenUserException("You can't see this user :)");
        }

        return modelMapper().map(user, UserView.class);
    }

    private UserEntity mapAsUser(UserRegisterBindingModel userRegisterBindingModel) {

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            return new UserEntity(); // TODO: better solution for field match or throw exception
        }

        UserEntity user = new UserEntity()
                .setEmail(userRegisterBindingModel.getEmail().trim())
                .setAge(userRegisterBindingModel.getAge())
                .setFirstName(userRegisterBindingModel.getFirstName().trim())
                .setLastName(userRegisterBindingModel.getLastName().trim())
                .setPassword(passwordEncoder().encode(userRegisterBindingModel.getPassword()))
                .setCreated(LocalDateTime.now())
                .setHotelInfoEntity(hotelService.getHotelInfo());

        if (roleService.getCount() == 0) {
            roleService.initRoles();
        }

        if (userRepository.count() == 0) {
            user.setRoles(roleService.getAllRoles());
        } else {
            user.setRoles(roleService.getAllRoles().stream().filter(r -> r.getName().equals(RoleEnum.USER)).toList());
        }

        return user;
    }

//todo: add hotelInfoEntity to this service to save logs, like when
    // user is made admin to save a message with date and who is making the post if it is possible

    @Override
    public void makeUserAdmin(String email) {
        // Find the user by email
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            // Fetch the ADMIN role
            RoleEntity adminRole = roleService.getAllRoles()
                    .stream()
                    .filter(role -> role.getName().name().equals("ADMIN"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));

            List<RoleEntity> allRoles = roleService.getAllRoles();

            UserEntity user = userOptional.get();

            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(role -> role.getName().name().equals(adminRole.getName().name()));

            // Check if the user already has the ADMIN role
            if (!isAdmin) {
                // Add the ADMIN role to the user
                user.setRoles(allRoles);
                userRepository.save(user);
            } else {
                System.out.println("User is already an admin.");
            }

        } else {
            // User not found
            throw new UserNotFoundException("User not found for email: " + email);
        }
    }

    @Override
    public void makeUserModerator(String email) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            if (user.getId() == 1) {
                throw new ForbiddenUserException("Don't try this.");
            }

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

        } else {
            throw new UserNotFoundException("User not found for email: " + email);
        }

    }

    @Override
    public void takeRights(String email) {
        RoleEntity userRole = roleService.getAllRoles()
                .stream()
                .filter(role -> role.getName().name().equals("USER"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("USER role not found"));

        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            if (user.getId() == 1) {
                throw new ForbiddenUserException("Don't try this.");
            }

            user.setRoles(List.of(userRole));
            userRepository.save(user);

        } else {
            throw new UserNotFoundException("User not found for email: " + email);
        }

    }

    public List<UserView> findAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .skip(1)
                .map(user -> modelMapper().map(user, UserView.class))
                .toList();
    }

    @Override
    public UserView findUserDetails(String userEmail) {
        UserEntity user = findUser(userEmail);

        return mapAsUserView(user);
    }

    private UserView mapAsUserView(UserEntity user) {
        UserView userView = new UserView()
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setFullName(user.getFirstName() + " " + user.getLastName())
                .setAge(user.getAge())
                .setEmail(user.getEmail())
                .setRoles(user.getRoles());

        Blob userImage = user.getUserImage();

        if (userImage != null) {
            try {
                userView.setUserImage(userImage.getBytes(1, (int) userImage.length()));
            } catch (SQLException ignored) {
            }
        }

        return userView;
    }

    @Override
    public void addUserImage(MultipartFile image, String userEmail, RedirectAttributes redirectAttributes) {
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
    public boolean editProfileInfo(EditUserProfileBindingModel editUserProfileBindingModel, String userEmail, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

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

        if (emailChanged) {
            // Invalidate the session
            request.getSession().invalidate();
            // Clear cookies
            Arrays.stream(request.getCookies()).forEach(cookie -> {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            });
        }
        reAuthenticateUser(editUserProfileBindingModel.getEmail());
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

        reAuthenticateUser(userEmail);
        redirectAttributes.addFlashAttribute("successMessage",
                "Password changed successfully.");

        return true;
    }

    private boolean isAllowedExtension(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        return extension.equals("jpg") ||
                extension.equals("jpeg") ||
                extension.equals("png") ||
                extension.equals("svg") ||
                extension.equals("avif") ||
                extension.equals("gif");
    }

    private void reAuthenticateUser(String newEmail) {
        // Load the new user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(newEmail);

        // Create a new authentication token
        UsernamePasswordAuthenticationToken newAuth =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getUsername(), userDetails.getAuthorities());

        newAuth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Set the new authentication token in the security context
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        // Update session with the new authentication
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }

    private UserEntity findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }
}
