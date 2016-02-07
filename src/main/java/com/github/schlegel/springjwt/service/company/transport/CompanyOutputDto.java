package com.github.schlegel.springjwt.service.company.transport;

import org.joda.time.DateTime;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CompanyOutputDto {

    private UUID id;
    private String name;
    private String address;
    private String description;
    private String mainContact;
    private Set<String> mailPostfixes;
    private DateTime createdAt;
    private DateTime updatedAt;
    private Boolean verified;
    private Boolean active;
    private Set<CompanyUserOutputDto> users = new HashSet<>();


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainContact() {
        return mainContact;
    }

    public void setMainContact(String mainContact) {
        this.mainContact = mainContact;
    }

    public Set<String> getMailPostfixes() {
        return mailPostfixes;
    }

    public void setMailPostfixes(Set<String> mailPostfixes) {
        this.mailPostfixes = mailPostfixes;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean isVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<CompanyUserOutputDto> getUsers() {
        return users;
    }

    public void setUsers(Set<CompanyUserOutputDto> users) {
        this.users = users;
    }
}
