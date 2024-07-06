package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.util.encryptionUtil.EncryptionService;
import com.HotelApp.util.encryptionUtil.EncryptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.HotelApp.config.ApplicationSecurityConfiguration.passwordEncoder;

@Service
public class UserTransformationService {
    private static final Logger log = LoggerFactory.getLogger(UserTransformationService.class);

    private final AppUserDetailsService userDetailsService;
    private final HttpServletRequest request;
    private final EncryptionService encryptionService;

    public UserTransformationService(AppUserDetailsService userDetailsService,
                                     HttpServletRequest request, EncryptionService encryptionService) {
        this.userDetailsService = userDetailsService;
        this.request = request;
        this.encryptionService = encryptionService;
    }

    @Cacheable(value = "userViewsCache", key = "'allUserViews'")
    public List<UserView> transformUsers(List<UserEntity> users) {
        log.info("Transforming users to user views");

        return users.stream()
                .map(this::mapAsUserView)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "userViewsCache", key = "'allUserViews'")
    public void evictUserViewsCache() {
        log.info("Evicting user views cache");
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
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }

    public boolean authenticateUser(String email, String password) throws Exception {
        try {
            String decryptedEmail = encryptionService.decrypt(email);
            String decryptedPassword = encryptionService.decrypt(password);

            // Load the user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(decryptedEmail);
            if (userDetails == null ||
                    !passwordEncoder().matches(decryptedPassword, userDetails.getPassword())) {
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
            request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            return true;
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }

    protected String decrypt(String encrypted) {
        try {
            return encryptionService.decrypt(encrypted);
        } catch (Exception ignored) {
            return "";
        }
    }
}
