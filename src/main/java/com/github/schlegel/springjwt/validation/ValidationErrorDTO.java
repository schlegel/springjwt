package com.github.schlegel.springjwt.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastianbayerl on 06/02/16.
 */
public class ValidationErrorDTO {

    private List<GlobalErrorDTO> globalErrors = new ArrayList<>();
    private List<FieldErrorDTO> fieldErrors = new ArrayList<>();


    public void addFieldError(String path, String message) {
        FieldErrorDTO error = new FieldErrorDTO(path, message);
        fieldErrors.add(error);
    }

    public void addGlobalError(String message) {
        GlobalErrorDTO error = new GlobalErrorDTO();
        globalErrors.add(error);
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }

    public List<GlobalErrorDTO> getGlobalErrors() {
        return globalErrors;
    }

}
