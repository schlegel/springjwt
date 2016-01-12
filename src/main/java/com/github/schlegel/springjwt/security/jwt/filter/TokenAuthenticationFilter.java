
package com.github.schlegel.springjwt.security.jwt.filter;

import com.github.schlegel.springjwt.security.jwt.service.AuthenticationServiceJWT;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TokenAuthenticationFilter extends GenericFilterBean {

    private Log logger = LogFactory.getLog(TokenAuthenticationFilter.class);

    @Value("${jwt.token.header}")
    private String headerToken;

    @Autowired private AuthenticationServiceJWT authenticationServiceJWT;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String token = httpRequest.getHeader(headerToken);

        if(token != null) {
            authenticationServiceJWT.authenticateWithJwtToken(token);
        }

        chain.doFilter(request, response);
    }
}
