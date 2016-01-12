package com.github.schlegel.springjwt.security.spring;

import com.github.schlegel.springjwt.model.User;
import com.github.schlegel.springjwt.model.UserRepository;
import com.github.schlegel.springjwt.security.AuthoritiesConstants;
import com.github.schlegel.springjwt.security.PrincipalUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(DaoUserDetailService.class);

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
        grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
        grantedAuthorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));

        // todo: check if the user is activated
        if (true) {
            return new PrincipalUser(user.getEmail(), user.getPassword(), user.getSalt(), grantedAuthorities,
                    user.getId());
        } else {
            return new PrincipalUser(user.getEmail(), user.getPassword(), user.getSalt(), grantedAuthorities,
                    user.getId(), false, true, true, true);
        }
    }
}