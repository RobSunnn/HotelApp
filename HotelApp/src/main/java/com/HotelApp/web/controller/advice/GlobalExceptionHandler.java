package com.HotelApp.web.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {
    //todo: you have some work here :)
    @ExceptionHandler({Throwable.class})
    public ModelAndView handleException(Throwable e) {
        ModelAndView modelAndView = new ModelAndView("error");
        ProblemDetail detail =
                ProblemDetail
                        .forStatusAndDetail(
                                HttpStatus.BAD_REQUEST, e.getMessage()
                        );

        modelAndView.addObject("detail", detail);

        return modelAndView;
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleAccessDenied(Throwable e) {
        ModelAndView modelAndView = new ModelAndView("error");
        ProblemDetail detail =
                ProblemDetail
                        .forStatusAndDetail(
                                HttpStatus.FORBIDDEN, e.getMessage()
                        );

        modelAndView.addObject("detail", detail);

        return modelAndView;
    }

    @ExceptionHandler(SQLException.class)
    public ModelAndView handleSqlError(Throwable e) {
        ModelAndView modelAndView = new ModelAndView("error");
        ProblemDetail detail =
                ProblemDetail
                        .forStatusAndDetail(
                                HttpStatus.CONFLICT, e.getMessage()
                        );

        detail.setDetail("BAAADDD ONE :)");

        modelAndView.addObject("detail", detail);

        return modelAndView;
    }

}
