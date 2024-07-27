package com.HotelApp.web.controller.rest;

import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.service.UserService;
import com.HotelApp.service.impl.UserTransformationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.HotelApp.common.constants.BindingConstants.BAD_CREDENTIALS;
import static com.HotelApp.common.constants.SuccessConstants.*;


@RestController
@RequestMapping("/users")
public class AuthenticationRestController {
    private final UserService userService;
    private final UserTransformationService userTransformationService;
    private final RequestCache requestCache = new HttpSessionRequestCache();

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
                                   @RequestParam("encryptedPass") String password,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean isSuccess = userTransformationService.authenticateUser(email, password);

        Map<String, Object> responseBody = new HashMap<>();
        if (!isSuccess) {
            responseBody.put("message", BAD_CREDENTIALS);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
        responseBody.put(SUCCESS, true);
        responseBody.put("message", LOGIN_SUCCESS);
        List<? extends GrantedAuthority> isAdmin = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
                .toList();
        String redirectUrl;
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            redirectUrl = savedRequest.getRedirectUrl();
        } else {
            redirectUrl = isAdmin.isEmpty() ? "/" : "/admin";
        }

        responseBody.put("redirectUrl", redirectUrl);
        return ResponseEntity.ok(responseBody);
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
            responseBody.put(SUCCESS, true);
            responseBody.put(REDIRECT_URL, "/users/registrationSuccess");
            return ResponseEntity.ok().body(responseBody);
        } else {
            responseBody.put(SUCCESS, false);
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
