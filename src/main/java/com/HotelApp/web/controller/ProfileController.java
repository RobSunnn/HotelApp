package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.ChangeUserPasswordBindingModel;
import com.HotelApp.domain.models.binding.EditUserProfileBindingModel;
import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        session.setAttribute("userToken", token);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userService.findUserDetails(userEmail);
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
    public String addProfilePicture(@RequestParam("profile-picture") MultipartFile image,
                                    RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        userService.addUserImage(image, userEmail, redirectAttributes);
        return "redirect:/users/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/editUserProfile")
    @ResponseBody
    public ResponseEntity<?> editProfile(@Valid EditUserProfileBindingModel editUserProfileBindingModel,
                                         BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        boolean editSuccessful = userService.editProfileInfo(
                editUserProfileBindingModel,
                userEmail,
                bindingResult,
                redirectAttributes
        );
        Map<String, Object> response = new HashMap<>();
        if (editSuccessful) {
            response.put("success", true);
            response.put("redirectUrl", "/users/profile/editSuccess");
            return ResponseEntity.ok().body(response);
        } else {
            response.put("success", false);
            response.put("errors", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/changePassword")
    @ResponseBody
    public ResponseEntity<?> changePasswordOfUser(@Valid ChangeUserPasswordBindingModel changeUserPasswordBindingModel,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        boolean changePasswordSuccessful = userService.changeUserPassword(
                userEmail,
                changeUserPasswordBindingModel,
                bindingResult,
                redirectAttributes
        );
        Map<String, Object> response = new HashMap<>();
        if (changePasswordSuccessful) {
            response.put("success", true);
            response.put("redirectUrl", "/users/profile");
            return ResponseEntity.ok().body(response);
        } else {
            response.put("success", false);
            response.put("errors", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

