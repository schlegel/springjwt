package com.github.schlegel.springjwt.permissions;

import com.github.schlegel.springjwt.security.AuthoritiesConstants;
import com.github.schlegel.springjwt.permissions.manager.DomainPermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserPermissionEvaluator implements DomainPermissionEvaluator {
    @Override
    public String getDomainId() {
        return "user";
    }

    @Override
    public boolean hasPermission(UUID targetId, String permission, List<String> roles, String user, Authentication authentication) {
        if(roles.contains(AuthoritiesConstants.SUPER_ADMIN)) {
            return true;
        }

        return false;
    }
}
