package com.github.schlegel.springjwt.security.spring;


import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder extends ShaPasswordEncoder {

    public PasswordEncoder() {
        super();
    }

    @Override
    public String encodePassword(String rawPass, Object salt) {
        String encoded = super.encodePassword(rawPass, salt);
        return encoded;
    }
}
