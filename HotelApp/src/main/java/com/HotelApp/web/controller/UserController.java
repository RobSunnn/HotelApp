package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.HotelApp.validation.constants.BindingConstants.*;


@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    public UserController(UserService userService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login-error")
    public String onFailure(@ModelAttribute("email") String email,
                            Model model) {

        model.addAttribute(EMAIL, email);
        model.addAttribute(BAD_CREDENTIALS, "true");

        return "login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/register")
    public String register(Model model) {

        if (!model.containsAttribute(USER_REGISTER_BINDING_MODEL)) {
            model.addAttribute(USER_REGISTER_BINDING_MODEL, new UserRegisterBindingModel());
        }

        return "register";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/register")
    public String register(@Valid UserRegisterBindingModel userRegisterBindingModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(USER_REGISTER_BINDING_MODEL, userRegisterBindingModel);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + USER_REGISTER_BINDING_MODEL, bindingResult);

            return "redirect:/users/register";
        }

        boolean registrationSuccessful = userService.registerUser(userRegisterBindingModel, bindingResult);

        if (registrationSuccessful) {
            return "redirect:/users/login";
        } else {
            return "register";
        }

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        UserEntity user = userService.findUserByEmail(userEmail);

        model.addAttribute("userDetails", user);

        return "profile";
    }
}
