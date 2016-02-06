package com.github.schlegel.springjwt.validation;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Created by sebastianbayerl on 06/02/16.
 */
@ControllerAdvice
public class RestErrorHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorDTO processValidationError(MethodArgumentNotValidException ex) {
        ValidationErrorDTO dto = new ValidationErrorDTO();
        BindingResult result = ex.getBindingResult();


        List<ObjectError> globalErrors = result.getGlobalErrors();
        for (ObjectError globalError : globalErrors) {
            dto.addGlobalError(globalError.getDefaultMessage());
        }

        List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            dto.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return dto;
    }

}
