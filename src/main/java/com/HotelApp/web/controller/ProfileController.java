package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.ChangeUserPasswordBindingModel;
import com.HotelApp.domain.models.binding.EditUserProfileBindingModel;
import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

import static com.HotelApp.common.constants.SuccessConstants.USER_TOKEN;

@Controller
@RequestMapping("/users/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addAttributes(HttpSession session) {
        String token = UUID.randomUUID().toString();
        session.setAttribute(USER_TOKEN, token);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public ModelAndView profile() {
        return new ModelAndView("users/profile");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/details")
    @ResponseBody
    public UserView getUserDetails() {
        return userService.findUserDetails();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/editProfile")
    public String editProfileInfo() {
        return "users/edit-profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/editSuccess")
    public String editProfileSuccess() {
        return "users/edit-profile-success";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/changePassword")
    public String changePassword() {
        return "users/change-password";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addProfileImage")
    public String addProfilePicture(
            @RequestParam("profile-picture") MultipartFile image,
            RedirectAttributes redirectAttributes
    ) {
        return userService.addUserImage(image, redirectAttributes);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/editUserProfile")
    @ResponseBody
    public ResponseEntity<?> editProfile(
            @Valid EditUserProfileBindingModel editUserProfileBindingModel,
            BindingResult bindingResult
    ) {
        return userService.editProfileInfo(editUserProfileBindingModel, bindingResult);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/changePassword")
    @ResponseBody
    public ResponseEntity<?> changePasswordOfUser(
            @Valid ChangeUserPasswordBindingModel changeUserPasswordBindingModel,
            BindingResult bindingResult
    ) {
        return userService.changeUserPassword(changeUserPasswordBindingModel, bindingResult);
    }
}

