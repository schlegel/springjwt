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
    private Boolean verified;
    private Boolean active;

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
}
