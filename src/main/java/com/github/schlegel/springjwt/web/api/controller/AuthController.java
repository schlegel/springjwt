package com.github.schlegel.springjwt.web.api.controller;

import com.github.schlegel.springjwt.security.jwt.service.AuthenticationServiceUsernamePassword;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class AuthController {

    @Value("${jwt.result}")
    private String defaultTokenResponse;

    @Autowired
    private AuthenticationServiceUsernamePassword authenticationServiceUsernamePassword;

    @RequestMapping(value = "/authentication", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> authenticate(String email, String password, HttpServletRequest request, HttpServletResponse httpResponse) {
        if (email != null && password != null) {
            try {
                SignedJWT token = authenticationServiceUsernamePassword.authenticate(email, password);

                if (token != null) {
                    return new ResponseEntity<String>(String.format(defaultTokenResponse, token.serialize()), HttpStatus.OK);
                } else {
                    // Authentication failed - Credentials dont match
                    return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
                }
            } catch (BadCredentialsException e) {
                // Authentication failed - Credentials dont match
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            // Authentication failed - No credentials provided
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }
    }
}
