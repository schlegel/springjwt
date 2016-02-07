package com.github.schlegel.springjwt.security.permissions;

import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface DomainPermissionEvaluator {

    String getDomainId();
    boolean hasPermission(UUID targetId, String permission, List<String> roles, String user, Authentication authentication);
}
