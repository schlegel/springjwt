
package com.github.schlegel.springjwt.security.jwt.service;

import com.github.schlegel.springjwt.security.PrincipalUser;
import com.github.schlegel.springjwt.security.jwt.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationServiceJWT {

    @Autowired
    private TokenManager tokenManager;

    public boolean authenticateWithJwtToken(final String token) {
        PrincipalUser principalUser = tokenManager.getUserDetails(token);

        if (principalUser != null) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principalUser, null, principalUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            return true;
        } else {
            return false;
        }
    }
}