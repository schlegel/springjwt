package com.github.schlegel.springjwt.validation;

import com.github.schlegel.springjwt.validation.transport.ValidationErrorOutputDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@ControllerAdvice
public class RestErrorHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorOutputDto processValidationError(MethodArgumentNotValidException ex) {
        ValidationErrorOutputDto dto = new ValidationErrorOutputDto();
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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorOutputDto processValidationError(ConstraintViolationException ex) {
        ValidationErrorOutputDto dto = new ValidationErrorOutputDto();
        Set<? extends ConstraintViolation<?>> result = ex.getConstraintViolations();

        for (ConstraintViolation<?> violation : result) {
            String field = violation.getPropertyPath().toString();

            if (field == null || field.equals("")) {
                dto.addGlobalError(violation.getMessage());
            } else {
                dto.addFieldError(field, violation.getMessage());
            }

        }

        return dto;
    }


}
