package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.ChangeUserPasswordBindingModel;
import com.HotelApp.domain.models.binding.EditUserProfileBindingModel;
import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserView user = userService.findUserDetails(userEmail);

        model.addAttribute("userDetails", user);
        model.addAttribute("userEmail", userEmail);

        EditUserProfileBindingModel editUserProfileBindingModel = new EditUserProfileBindingModel();
        editUserProfileBindingModel.setFirstName(user.getFirstName());
        editUserProfileBindingModel.setLastName(user.getLastName());
        editUserProfileBindingModel.setEmail(user.getEmail());
        editUserProfileBindingModel.setAge(user.getAge());

        if (!model.containsAttribute("editUserProfileBindingModel")) {
            model.addAttribute("editUserProfileBindingModel", editUserProfileBindingModel);
        }
        if (!model.containsAttribute("changeUserPasswordBindingModel")) {
            model.addAttribute("changeUserPasswordBindingModel", new ChangeUserPasswordBindingModel());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public String profile() {
        return "users/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/editProfile")
    public String editProfileInfo() {
        return "users/edit-profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/changePassword")
    public String changePassword() {
        return "users/change-password";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addProfileImage")
    public String addProfilePicture(@RequestParam("profile-picture") MultipartFile image,
                                    RedirectAttributes redirectAttributes,
                                    @ModelAttribute("userEmail") String userEmail) {

        userService.addUserImage(image, userEmail);
        redirectAttributes.addFlashAttribute("successMessage", "Profile picture uploaded successfully.");

        return "redirect:/users/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/editUserProfile")
    public String editProfile(@Valid EditUserProfileBindingModel editUserProfileBindingModel,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              @ModelAttribute("userEmail") String userEmail) {
//todo: when validation fail use binding result and redirect att
        userService.editProfileInfo(editUserProfileBindingModel, userEmail);

        return "redirect:/users/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/changePassword")
    public String changePasswordOfUser(@Valid ChangeUserPasswordBindingModel changeUserPasswordBindingModel,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes,
                                       @ModelAttribute("userEmail") String userEmail) {

        userService.changeUserPassword(userEmail, changeUserPasswordBindingModel);

        return "redirect:/users/profile";
    }
}
