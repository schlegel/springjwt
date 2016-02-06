package com.github.schlegel.springjwt.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PermissionManager implements PermissionEvaluator {

    private final Logger logger = LoggerFactory.getLogger(PermissionManager.class);

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        logger.debug(authentication.getPrincipal().toString() + " wants permission " + permission.toString() +  " for " + targetType + " with id " + targetId);

        List<String> roles = authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());

        if("company".equals(targetType)) {
            if(roles.contains(AuthoritiesConstants.SUPER_ADMIN)) {
                return true;
            }
        }

        return false;
    }
}
