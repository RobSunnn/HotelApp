package com.HotelApp.util.filter;

import com.HotelApp.util.encryptionUtil.EncryptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.setFilterProcessesUrl("/perform_login");
        this.setAuthenticationSuccessHandler(new CustomSuccessHandler()); // Custom success handler for redirection
        this.setAuthenticationFailureHandler(new CustomFailureHandler());
        super.setAuthenticationManager(authenticationManager);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String encryptedUsername = request.getParameter("encryptedEmail");
        String encryptedPassword = request.getParameter("encryptedPass");
        String key = request.getParameter("key");
        String iv = request.getParameter("iv");
        String decryptedUsername;
        String decryptedPassword;
        try {
            decryptedUsername = decrypt(encryptedUsername, key, iv);
            decryptedPassword = decrypt(encryptedPassword, key, iv);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(decryptedUsername, decryptedPassword);
        setDetails(request, authRequest);
        Authentication authenticate = this.authenticationManager.authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

// Get the current SecurityContext
        SecurityContext context = SecurityContextHolder.getContext();

// Optionally, set the SecurityContext in HttpSession
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", context);

        return authenticate;
    }

    private String decrypt(String encrypted, String key, String iv) throws Exception {
        return EncryptionUtil.decrypt(encrypted, iv, key);  // Ensure correct order of parameters
    }

    private static class CustomSuccessHandler implements AuthenticationSuccessHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authentication) throws IOException {
            // Redirect to home page or another success URL
            response.sendRedirect("/");

        }
    }

    private static class CustomFailureHandler implements AuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
            // Redirect or handle failure as needed (e.g., show error message)
            response.sendRedirect("/login?error=true");
        }
    }
}