//package com.HotelApp.web.controller.advice;
//
//import org.springframework.expression.AccessException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ProblemDetail;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.sql.SQLException;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    //todo: you have some work here :)
//    public ModelAndView handleException(Throwable e) {
//        ModelAndView modelAndView = new ModelAndView("error");
//
//
//
//        return modelAndView;
//    }
//
////    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
////    public ModelAndView handleAccessDenied(Throwable e) {
////        ModelAndView modelAndView = new ModelAndView("error");
////        ProblemDetail detail =
////                ProblemDetail
////                        .forStatusAndDetail(
////                                HttpStatus.FORBIDDEN, e.getMessage()
////                        );
////
////        modelAndView.addObject("detail", detail);
////
////        return modelAndView;
////    }
//
//}
