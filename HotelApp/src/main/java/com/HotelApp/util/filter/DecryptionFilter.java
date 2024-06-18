package com.HotelApp.util.filter;

import com.HotelApp.util.encryptionUtil.EncryptionUtil;
import jakarta.servlet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class DecryptionFilter implements Filter {

    private final ApplicationContext applicationContext;

    @Autowired
    public DecryptionFilter(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
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

                UserDetailsService userDetailsService = applicationContext.getBean(UserDetailsService.class);
                PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);

                // Manually authenticate the user
                UserDetails userDetails = userDetailsService.loadUserByUsername(decryptedUsername);
                if (userDetails != null &&
                        passwordEncoder.matches(decryptedPassword, userDetails.getPassword())) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    request.setAttribute("LOGIN_ERROR_FLAG", "true");
                    throw new RuntimeException("Bad Credentials");
                }
                request.setAttribute("username", decryptedUsername);
                request.setAttribute("password", decryptedPassword);
            } catch (Exception e) {
                request.setAttribute("LOGIN_ERROR_FLAG", "true");
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}


}
