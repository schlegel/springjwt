package com.github.schlegel.springjwt.model;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserRepository {

    public User findByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        user.setId(UUID.randomUUID().toString());
        // password = password
        user.setPassword("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8");
        user.setSalt(null);
        user.setUsername("user1");

        return user;
    }
}
