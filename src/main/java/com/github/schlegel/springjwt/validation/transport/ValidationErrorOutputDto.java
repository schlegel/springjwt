package com.github.schlegel.springjwt.validation.transport;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates field and global errors.
 */
public class ValidationErrorOutputDto {

    private List<GlobalErrorOutputDto> globalErrors = new ArrayList<>();
    private List<FieldErrorOutputDto> fieldErrors = new ArrayList<>();

    public void addFieldError(String path, String message) {
        FieldErrorOutputDto error = new FieldErrorOutputDto(path, message);
        fieldErrors.add(error);
    }

    public void addGlobalError(String message) {
        GlobalErrorOutputDto error = new GlobalErrorOutputDto(message);
        globalErrors.add(error);
    }

    public List<FieldErrorOutputDto> getFieldErrors() {
        return fieldErrors;
    }

    public List<GlobalErrorOutputDto> getGlobalErrors() {
        return globalErrors;
    }

}
