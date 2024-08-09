package com.HotelApp.util;

import com.HotelApp.domain.entity.ForbiddenRequestEntity;
import com.HotelApp.repository.ForbiddenRequestRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.LocalDateTime;

public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);
    private static final String FORBIDDEN_REQUEST_LOG_INFO = "Request resulted in 403 Forbidden: URL = my-host.com{}, Method = {}, IP = {}, Username = {}";

    private final ForbiddenRequestRepository forbiddenRequestRepository;

    public LoggingInterceptor(ForbiddenRequestRepository forbiddenRequestRepository) {
        this.forbiddenRequestRepository = forbiddenRequestRepository;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) throws IOException {
        int statusCode = response.getStatus();

        if (statusCode == HttpServletResponse.SC_FORBIDDEN) {
            String username = getUsername();

            String originalUrl = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
            log.warn(FORBIDDEN_REQUEST_LOG_INFO, originalUrl, request.getMethod(), getPublicIpAddress(), username);

            ForbiddenRequestEntity forbiddenRequest = new ForbiddenRequestEntity()
                    .setUrl(originalUrl)
                    .setMethod(request.getMethod())
                    .setIp(getPublicIpAddress())
                    .setUsername(username)
                    .setTimestamp(LocalDateTime.now())
                    .setChecked(false);

            forbiddenRequestRepository.save(forbiddenRequest);
        }
    }

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                return userDetails.getUsername();
            } else {
                return authentication.getPrincipal().toString();
            }
        }
        return "Anonymous";
    }


    private String getPublicIpAddress() throws IOException {
        return PublicIpFetcher.getPublicIpAddress();
    }
}