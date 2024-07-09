package com.HotelApp.web.controller.rest;

import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.service.UserService;
import com.HotelApp.service.impl.UserTransformationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class AuthenticationRestController {
    private static final String LOGIN_ERROR_FLAG = "LOGIN_ERROR_FLAG";

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
    public ResponseEntity<?> login(@RequestParam("encryptedEmail") String email,
                                   @RequestParam("encryptedPass") String password) throws Exception {
        boolean isSuccess = userTransformationService.authenticateUser(email, password);

        Map<String, Object> response = new HashMap<>();
        if (!isSuccess) {
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("success", true);
        response.put("message", "Login success");
        response.put("redirectUrl", "/");
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/register", produces = "text/html")
    public ModelAndView register() {
        return new ModelAndView("users/register");
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping(value = "/register", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> register(@Valid UserRegisterBindingModel userRegisterBindingModel,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {

        boolean registrationSuccessful = userService.registerUser(
                userRegisterBindingModel,
                bindingResult,
                redirectAttributes
        );

        Map<String, Object> responseBody = new HashMap<>();
        if (registrationSuccessful) {
            responseBody.put("success", true);
            responseBody.put("redirectUrl", "/users/registrationSuccess");
            return ResponseEntity.ok().body(responseBody);
        } else {
            responseBody.put("success", false);
            responseBody.put("errors", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(responseBody);
        }

    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/registrationSuccess", produces = "text/html")
    public ModelAndView registrationSuccess() {
        return new ModelAndView("users/registration-success");
    }
}
