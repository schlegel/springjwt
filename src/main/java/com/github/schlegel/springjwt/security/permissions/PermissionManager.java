package com.github.schlegel.springjwt.security.permissions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PermissionManager implements PermissionEvaluator {

    private final Logger logger = LoggerFactory.getLogger(PermissionManager.class);

    private Map<String, DomainPermissionEvaluator> permissionEvaluatorMap;

    @Autowired
    public PermissionManager(List<DomainPermissionEvaluator> permissionEvaluators) {
        this.permissionEvaluatorMap = new HashMap<>();

        for(DomainPermissionEvaluator permissionEvaluator : permissionEvaluators) {
            this.permissionEvaluatorMap.put(permissionEvaluator.getDomainId(), permissionEvaluator);
        }

    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        throw new IllegalStateException("TargetDomainObject permission evaluation mode not supported");
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        logger.debug(authentication.getName() + " wants permission " + permission.toString() +  " for " + targetType + " with id " + targetId);

        List<String> roles = authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());

        if(permissionEvaluatorMap.containsKey(targetType)) {
            if((targetId == null ||targetId instanceof UUID) && permission instanceof String) {
                return permissionEvaluatorMap.get(targetType).hasPermission((UUID)targetId, (String) permission, roles, authentication.getName());
            } else {
                throw new IllegalArgumentException("Wrong object types for permission or targetId");
            }

        } else {
            logger.error("No permission evaluator " + targetType + "available");
            return false;
        }
    }
}
