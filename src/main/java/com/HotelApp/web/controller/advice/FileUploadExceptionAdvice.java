package com.HotelApp.web.controller.advice;

import com.HotelApp.service.exception.FileNotAllowedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.HotelApp.common.constants.FailConstants.ERROR_MESSAGE;


@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ERROR_MESSAGE, exc.getBody().getDetail() + "!");

        return "redirect:/users/profile";
    }

    @ExceptionHandler(FileNotAllowedException.class)
    public String handleFileNotAllowed(FileNotAllowedException exc, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ERROR_MESSAGE, exc.getMessage());

        return "redirect:/users/profile";
    }
}
