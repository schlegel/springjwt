package com.github.schlegel.springjwt.service.company.transport;

import com.github.schlegel.springjwt.security.AuthoritiesConstants;
import com.github.schlegel.springjwt.validation.annotations.RoleRestriction;

import javax.validation.constraints.Size;

public class CompanyUpdateDto {

    @RoleRestriction(AuthoritiesConstants.SUPER_ADMIN)
    @Size(min = 3)
    private String name;

    private String address;

    private String description;

    @RoleRestriction(AuthoritiesConstants.SUPER_ADMIN)
    private String mainContact;

    @RoleRestriction(AuthoritiesConstants.SUPER_ADMIN)
    private Boolean verified;

    @RoleRestriction(AuthoritiesConstants.SUPER_ADMIN)
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
