package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

import static com.HotelApp.common.constants.BindingConstants.*;


@RestController
@RequestMapping("/users")
public class AuthenticationController {
    private static final String LOGIN_ERROR_FLAG = "LOGIN_ERROR_FLAG";
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        if (!model.containsAttribute(USER_REGISTER_BINDING_MODEL)) {
            model.addAttribute(USER_REGISTER_BINDING_MODEL, new UserRegisterBindingModel());
        }
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/login", produces = "text/html")
    public ModelAndView login() {
        return new ModelAndView("users/login");
    }


    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(HttpServletRequest request) {
        String loginErrorFlag = request.getAttribute(LOGIN_ERROR_FLAG).toString();

        Map<String, String> response = new HashMap<>();
        if ("true".equals(loginErrorFlag)) {
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.put("message", "Login success");
//        String test = SecurityContextHolder.getContext().getAuthentication().getName();
//        if (test != null) {
//            log.info("User with email have logged in: {}", test);
//        }

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/register", produces = "text/html")
    public ModelAndView register() {
        return new ModelAndView("users/register");
    }


    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/registrationSuccess", produces = "text/html")
    public ModelAndView registrationSuccess() {
        return new ModelAndView("users/registration-success");
    }


    @PreAuthorize("isAnonymous()")
    @PostMapping(value = "/register", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> register(@Valid UserRegisterBindingModel userRegisterBindingModel,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {

        boolean registrationSuccessful = userService
                .registerUser(userRegisterBindingModel, bindingResult, redirectAttributes);

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
}
