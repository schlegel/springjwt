package com.github.schlegel.springjwt.service.company;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CompanyDTO {

    private UUID id;

    @NotNull
    private String name;
    private String address;
    private String description;
    private String mainContact;

    @NotEmpty
    private Set<String> mailPostfixes = new HashSet<>();
    private DateTime createdAt;
    private DateTime updatedAt;
    private boolean verified;
    private boolean active;



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

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
