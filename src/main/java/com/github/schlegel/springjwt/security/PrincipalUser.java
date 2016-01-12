package com.github.schlegel.springjwt.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class PrincipalUser extends User {

    private String id;
    private String salt;

    public PrincipalUser(String username, String password, String salt,
                         Collection<? extends GrantedAuthority> authorities, String id ) {
        super(username, password, authorities);
        this.id = id;
        this.salt = salt;
    }

    public PrincipalUser(String username, String password, String salt,
                         Collection<? extends GrantedAuthority> authorities, String id, boolean enabled,
                         boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.salt = salt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
