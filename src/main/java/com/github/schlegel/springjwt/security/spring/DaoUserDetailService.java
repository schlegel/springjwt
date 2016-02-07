package com.github.schlegel.springjwt.security.spring;

import com.github.schlegel.springjwt.domain.user.User;
import com.github.schlegel.springjwt.domain.user.UserRepository;
import com.github.schlegel.springjwt.domain.user.UserRole;
import com.github.schlegel.springjwt.security.AuthoritiesConstants;
import com.github.schlegel.springjwt.security.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class DaoUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public DaoUserDetailService() {
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // check if login email available
        if (email.trim().isEmpty()) {
            throw new IllegalArgumentException("No login email available");
        }

        // fetch user from database
        String lowercaseEmail = email.toLowerCase();
        User userFromDatabase = userRepository.findByEmail(lowercaseEmail);

        // check if user is in database
        if (userFromDatabase == null) {
            throw new UsernameNotFoundException("User " + lowercaseEmail + " was not found in the database");
        }

        return getPrincipalUser(userFromDatabase);
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static PrincipalUser getPrincipalUser(User user) {
        // add roles to the user
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

        if(UserRole.USER.equals(user.getRole())) {
            grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
            grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
        } else if(UserRole.COMPANY_ADMIN.equals(user.getRole())) {
            grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
            grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
            grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.COMPANY_ADMIN));
        } else if(UserRole.SUPER_ADMIN.equals(user.getRole())) {
            grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
            grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
            grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.COMPANY_ADMIN));
            grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.SUPER_ADMIN));
        }

        return new PrincipalUser(user.getEmail(), user.getPassword(), user.getSalt(), grantedAuthorities, user.getId().toString());
    }
}