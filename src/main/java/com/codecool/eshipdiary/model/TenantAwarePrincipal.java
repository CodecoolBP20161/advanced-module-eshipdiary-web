package com.codecool.eshipdiary.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TenantAwarePrincipal extends org.springframework.security.core.userdetails.User {
    public Club club;
    public TenantAwarePrincipal(String username, String password, Collection<? extends GrantedAuthority> authorities, Club club) {
        super(username, password, authorities);
        this.club = club;
    }

    public TenantAwarePrincipal(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Club club) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.club = club;
    }
}