package com.github.schlegel.springjwt.service.company.transport;

import com.github.schlegel.springjwt.validation.CheckCompany;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Set;

@CheckCompany
public class CompanyCreateDto {

    @NotNull
    private String name;
    private String address;
    private String description;
    private String mainContact;

    @NotEmpty
    private Set<String> mailPostfixes;
    private boolean verified;
    private boolean active;

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
