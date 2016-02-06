package com.github.schlegel.springjwt.security.permissions.impl;

import com.github.schlegel.springjwt.domain.company.CompanyRepository;
import com.github.schlegel.springjwt.domain.user.User;
import com.github.schlegel.springjwt.domain.user.UserRepository;
import com.github.schlegel.springjwt.security.AuthoritiesConstants;
import com.github.schlegel.springjwt.security.permissions.DomainPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CompanyPermissionEvaluator implements DomainPermissionEvaluator {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public String getDomainId() {
        return "company";
    }

    @Override
    public boolean hasPermission(UUID targetId, String permission, List<String> roles, String user, Authentication authentication) {
            if(roles.contains(AuthoritiesConstants.SUPER_ADMIN)) {
                return true;
            } else if(roles.contains(AuthoritiesConstants.COMPANY_ADMIN)) {
                User userEntity = userRepository.findByEmailAndCompanyId(user, targetId);
                return userEntity != null;
            }

        return false;
    }
}
