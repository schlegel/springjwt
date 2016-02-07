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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * Handles Exceptions that are thrown during validation.
 */
@ControllerAdvice
public class RestErrorHandler {

    /**
     * Processes the exceptions that are thrown during validation on CONTROLLER level.
     *
     * @param ex The validation on controller level throws MethodArgumentNotValidExceptions.
     * @return The validation errors encapsulated in a ValidationErrorOutputDto object.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Validation Error")
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

    /**
     * Processes the exceptions that are thrown during validation on SERVICE level.
     *
     * @param ex The validation on service level throws ConstraintViolationExceptions.
     * @return The validation errors encapsulated in a ValidationErrorOutputDto object.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Validation Error")
    @ResponseBody
    public ValidationErrorOutputDto processValidationError(ConstraintViolationException ex) {
        ValidationErrorOutputDto dto = new ValidationErrorOutputDto();
        Set<? extends ConstraintViolation<?>> result = ex.getConstraintViolations();

        for (ConstraintViolation<?> violation : result) {
            // only use the field name
            String[] splits = violation.getPropertyPath().toString().split("\\.");
            String field = splits[splits.length - 1];

            if (field == null || field.equals("")) {
                dto.addGlobalError(violation.getMessage());
            } else {
                dto.addFieldError(field, violation.getMessage());
            }
        }

        return dto;
    }

    /**
     * Processes the exceptions that are thrown during the type conversion in the input object generation phase.
     *
     * @param ex The type conversion in the input object generation phase throws MethodArgumentTypeMismatchException.
     * @return The conversion errors encapsulated in a ValidationErrorOutputDto object.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Conversion Error")
    @ResponseBody
    public ValidationErrorOutputDto processValidationError(MethodArgumentTypeMismatchException ex) {
        ValidationErrorOutputDto dto = new ValidationErrorOutputDto();
        dto.addFieldError(ex.getName(), "Type conversion error occured.");

        return dto;
    }

}
