package com.HotelApp.util.filter;

import com.HotelApp.service.impl.AppUserDetailsService;
import com.HotelApp.util.encryptionUtil.EncryptionUtil;
import jakarta.servlet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.io.IOException;

import static com.HotelApp.config.ApplicationSecurityConfiguration.passwordEncoder;

@Component
public class DecryptionFilter implements Filter {

    private final AppUserDetailsService appUserDetailsService;


    @Autowired
    public DecryptionFilter(AppUserDetailsService appUserDetailsService) {
        this.appUserDetailsService = appUserDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String encryptedUsername = request.getParameter("encryptedEmail");
        String encryptedPassword = request.getParameter("encryptedPass");
        String iv = request.getParameter("iv");
        String key = request.getParameter("key");

        if (encryptedUsername != null && encryptedPassword != null) {
            try {
                String decryptedUsername = EncryptionUtil.decrypt(encryptedUsername, iv, key);
                String decryptedPassword = EncryptionUtil.decrypt(encryptedPassword, iv, key);

                // Manually authenticate the user
                UserDetails userDetails = appUserDetailsService.loadUserByUsername(decryptedUsername);
                if (userDetails != null &&
                        passwordEncoder().matches(decryptedPassword, userDetails.getPassword())) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    request.setAttribute("LOGIN_ERROR_FLAG", "false");
                } else {
                    request.setAttribute("LOGIN_ERROR_FLAG", "true");
                    throw new UsernameNotFoundException("Invalid username or password.");
                }
                request.setAttribute("username", decryptedUsername);
                request.setAttribute("password", decryptedPassword);
            } catch (Exception e) {
                request.setAttribute("LOGIN_ERROR_FLAG", "true");
                // Handle decryption failure or any other exceptions
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}