package com.github.schlegel.springjwt.service.company.transport;

import com.github.schlegel.springjwt.domain.user.UserRole;

import java.util.UUID;

public class CompanyUserOutputDto {

    private UUID id;
    private String username;
    private String email;
    private UserRole role;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
