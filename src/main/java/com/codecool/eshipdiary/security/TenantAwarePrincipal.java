package com.codecool.eshipdiary.security;

import com.codecool.eshipdiary.model.Club;
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

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }
}