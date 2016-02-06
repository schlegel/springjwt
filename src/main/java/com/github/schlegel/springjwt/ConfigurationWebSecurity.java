package com.github.schlegel.springjwt;

import com.github.schlegel.springjwt.security.jwt.filter.TokenAuthenticationFilter;
import com.github.schlegel.springjwt.security.spring.Http401UnauthorizedEntryPoint;
import com.github.schlegel.springjwt.security.spring.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class ConfigurationWebSecurity extends WebSecurityConfigurerAdapter {


    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint;
    @Autowired private UserDetailsService daoUserDetailService;
    @Autowired @Lazy private TokenAuthenticationFilter tokenAuthenticationFilter;


    @Bean
    public DaoAuthenticationProvider customAuthenticationProvider() {
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("salt");

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(daoUserDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setSaltSource(saltSource);

        return authenticationProvider;
    }

    @Bean
    @Qualifier("customAuthenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // or BasicAuthenticationFilter
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.httpBasic()
            .authenticationEntryPoint(http401UnauthorizedEntryPoint)
        .and()
            .anonymous()
        .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .headers().frameOptions().disable()
        .and()
            .csrf().disable();
    }
}
