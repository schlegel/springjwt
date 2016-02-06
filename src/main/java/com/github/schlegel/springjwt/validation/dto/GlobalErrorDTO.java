package com.github.schlegel.springjwt.validation.dto;

public class GlobalErrorDTO {

    private String message;

    public GlobalErrorDTO(String message) {
        this.message = message;

    }

    public String getMessage() {
        return message;
    }
}
