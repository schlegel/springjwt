package com.github.schlegel.springjwt.web.api.controller;

import com.github.schlegel.springjwt.security.AuthoritiesConstants;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    @ResponseBody
    @Secured(AuthoritiesConstants.ANONYMOUS)
    public String getHelloWorld() {
        return "Hello  World";
    }

    @RequestMapping(value = "/securedping", method = RequestMethod.GET)
    @ResponseBody
    @Secured(AuthoritiesConstants.USER)
    public String getSecuredHelloWorld() {
        return "Hello Secured World";
    }

    @RequestMapping(value = "/security", method = RequestMethod.GET)
    @ResponseBody
    public Object getAuthStatus() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

}
