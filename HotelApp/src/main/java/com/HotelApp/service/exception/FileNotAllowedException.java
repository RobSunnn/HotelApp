package com.HotelApp.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class FileNotAllowedException extends RuntimeException {

    public FileNotAllowedException(String message) {
        super(message);
    }
}
