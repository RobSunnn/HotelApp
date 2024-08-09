package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.util.encryptionUtil.EncryptionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.HotelApp.common.constants.BindingConstants.BAD_CREDENTIALS;
import static com.HotelApp.common.constants.InfoConstants.USERS_CACHE;
import static com.HotelApp.common.constants.SuccessConstants.*;
import static com.HotelApp.config.ApplicationBeanConfiguration.passwordEncoder;

@Service
public class UserTransformationService {
    private static final Logger log = LoggerFactory.getLogger(UserTransformationService.class);
    private static final String SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    private final AppUserDetailsService userDetailsService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final EncryptionService encryptionService;
    private final RequestCache requestCache = new HttpSessionRequestCache();

    public UserTransformationService(
            AppUserDetailsService userDetailsService,
            HttpServletRequest request,
            HttpServletResponse response,
            EncryptionService encryptionService
    ) {
        this.userDetailsService = userDetailsService;
        this.request = request;
        this.response = response;
        this.encryptionService = encryptionService;
    }

    @Cacheable(value = USERS_CACHE, key = "'allUserViews'")
    public List<UserView> transformUsers(List<UserEntity> users) {
        log.info(TRANSFORMING_USERS);

        return users.stream()
                .map(this::mapAsUserView)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = USERS_CACHE, key = "'allUserViews'")
    public void evictUserViewsCache() {
        log.info(EVICTING_USERS);
    }

    protected UserView mapAsUserView(UserEntity user) {
        UserView userView = new UserView()
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setFullName(user.getFirstName() + " " + user.getLastName())
                .setAge(user.getAge())
                .setRoles(user.getRoles());

        try {
            String encryptedEmail = encryptionService.encrypt(user.getEmail());
            userView.setEncryptedEmail(encryptedEmail);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Blob userImage = user.getUserImage();
        if (userImage != null) {
            try {
                userView.setUserImage(userImage.getBytes(1, (int) userImage.length()));
            } catch (SQLException ignored) {
            }
        }

        return userView;
    }

    protected UserEntity mapAsUser(UserRegisterBindingModel userRegisterBindingModel) {
        return new UserEntity()
                .setEmail(userRegisterBindingModel.getEmail().trim())
                .setAge(userRegisterBindingModel.getAge())
                .setFirstName(userRegisterBindingModel.getFirstName().trim())
                .setLastName(userRegisterBindingModel.getLastName().trim())
                .setPassword(passwordEncoder().encode(userRegisterBindingModel.getPassword()))
                .setCreated(LocalDateTime.now());
    }

    public void reAuthenticateUser(String email) {
        // Load the user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        // Create an authentication token
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getUsername(),
                userDetails.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // Set the new authentication token in the security context
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // Update session with the new authentication
        request.getSession().setAttribute(SECURITY_CONTEXT, SecurityContextHolder.getContext());
    }

    public boolean authenticateUser(String email, String password) {
        try {
            String decryptedEmail = encryptionService.decrypt(email);
            String decryptedPassword = encryptionService.decrypt(password);

            // Load the user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(decryptedEmail);
            if (userDetails == null || !passwordEncoder().matches(decryptedPassword, userDetails.getPassword())) {
                return false;
            }
            // Create an authentication token
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.getUsername(),
                    userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Set the new authentication token in the security context
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            // Update session with the new authentication
            request.getSession().setAttribute(SECURITY_CONTEXT, SecurityContextHolder.getContext());

            return true;
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }

    public ResponseEntity<Map<String, Object>> loginResponse(boolean isSuccess) {
        Map<String, Object> responseBody = new HashMap<>();
        if (!isSuccess) {
            responseBody.put(MESSAGE, BAD_CREDENTIALS);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }

        responseBody.put(SUCCESS, true);
        responseBody.put(MESSAGE, LOGIN_SUCCESS);

        String redirectUrl;
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            redirectUrl = savedRequest.getRedirectUrl();
        } else {
            redirectUrl = checkIfIsAdmin() ? "/" : "/admin";
        }
        responseBody.put(REDIRECT_URL, redirectUrl);

        return ResponseEntity.ok(responseBody);
    }

    private boolean checkIfIsAdmin() {
        List<? extends GrantedAuthority> isAdmin = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
                .toList();

        return isAdmin.isEmpty();
    }

    protected String decrypt(String encrypted) {
        try {
            return encryptionService.decrypt(encrypted);
        } catch (Exception ignored) {
            return "";
        }
    }
}
