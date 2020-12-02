package com.stn.ester.core.exceptions.advices;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    public final ModelAndView handleConstraintViolationException(ConstraintViolationException ex, WebRequest request, HttpServletResponse response) {
        try {
            response.sendError(400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ModelAndView();
    }
}
