package com.github.schlegel.springjwt.validation.dto;

/**
 * Created by sebastianbayerl on 06/02/16.
 */
public class FieldErrorDTO {

    private String field;

    private String message;

    public FieldErrorDTO(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}