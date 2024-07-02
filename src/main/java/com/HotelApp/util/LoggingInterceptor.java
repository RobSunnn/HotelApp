package com.HotelApp.util;

import com.HotelApp.domain.entity.ForbiddenRequestEntity;
import com.HotelApp.repository.ForbiddenRequestRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    private final ForbiddenRequestRepository forbiddenRequestRepository;

    public LoggingInterceptor(ForbiddenRequestRepository forbiddenRequestRepository) {
        this.forbiddenRequestRepository = forbiddenRequestRepository;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        int statusCode = response.getStatus();

        if (statusCode == HttpServletResponse.SC_FORBIDDEN) {
            String username = getUsername();

            String originalUrl = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
            log.warn("Request resulted in 403 Forbidden: URL = my-host.com{}, Method = {}, IP = {}, Username = {}",
                    originalUrl, request.getMethod(), getClientIpAddress(request), username);

            ForbiddenRequestEntity forbiddenRequest = new ForbiddenRequestEntity()
                    .setUrl(originalUrl)
                    .setMethod(request.getMethod())
                    .setIp(getClientIpAddress(request))
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

    private String getClientIpAddress(@NotNull HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {

            return ip.split(",")[0].trim();
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
