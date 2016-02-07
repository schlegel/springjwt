package com.github.schlegel.springjwt.validation.transport;


public class GlobalErrorOutputDto {

    private String message;

    public GlobalErrorOutputDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
