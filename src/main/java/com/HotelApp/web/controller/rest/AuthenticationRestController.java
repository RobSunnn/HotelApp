package com.HotelApp.web.controller.rest;

import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.service.UserService;
import com.HotelApp.service.impl.UserTransformationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/users")
public class AuthenticationRestController {
    private final UserService userService;
    private final UserTransformationService userTransformationService;

    public AuthenticationRestController(UserService userService, UserTransformationService userTransformationService) {
        this.userService = userService;
        this.userTransformationService = userTransformationService;
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/login", produces = "text/html")
    public ModelAndView login() {
        return new ModelAndView("users/login");
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(
            @RequestParam("encryptedEmail") String email,
            @RequestParam("encryptedPass") String password
    ) throws Exception {
        boolean isSuccess = userTransformationService.authenticateUser(email, password);
        return userTransformationService.loginResponse(isSuccess);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/register", produces = "text/html")
    public ModelAndView register() {
        return new ModelAndView("users/register");
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping(value = "/register", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> register(
            @Valid UserRegisterBindingModel userRegisterBindingModel,
            BindingResult bindingResult
    ) {
        return userService.registerUser(userRegisterBindingModel, bindingResult);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/registrationSuccess", produces = "text/html")
    public ModelAndView registrationSuccess() {
        return new ModelAndView("users/registration-success");
    }
}
