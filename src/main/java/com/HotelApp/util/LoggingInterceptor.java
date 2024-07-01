package com.HotelApp.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
//        String ipAddress = getClientIpAddress(request);
//        log.info("Incoming request: URL = {}, Method = {}, IP = {}",
//                request.getRequestURL(), request.getMethod(), ipAddress);
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           @NonNull Object handler,
                           ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        Long startTime = (Long) request.getAttribute("startTime");
        if (startTime != null) {
            long endTime = System.currentTimeMillis();
            long executeTime = endTime - startTime;

            int statusCode = response.getStatus();
//            log.info("Outgoing response: Status = {}, Time Taken = {} ms",
//                    response.getStatus(), executeTime);
//            Object handler1 = handler;
            if (statusCode == HttpServletResponse.SC_FORBIDDEN) {
                log.warn("Request resulted in 403 Forbidden: URL = {}, Method = {}, IP = {}",
                        request.getRequestURL(), request.getMethod(), getClientIpAddress(request));
            }
        }
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
