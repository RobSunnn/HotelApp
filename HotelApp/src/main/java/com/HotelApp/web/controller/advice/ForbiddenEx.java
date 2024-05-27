package com.HotelApp.web.controller.advice;

import org.springframework.http.HttpStatus;

public class ForbiddenEx extends RuntimeException{
    private HttpStatus status;
    public ForbiddenEx(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
      return status;
    }
}
