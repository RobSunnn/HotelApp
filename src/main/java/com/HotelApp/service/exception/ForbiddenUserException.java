package com.HotelApp.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenUserException extends RuntimeException {

    public ForbiddenUserException(String message) {
        super(message);
    }
}
