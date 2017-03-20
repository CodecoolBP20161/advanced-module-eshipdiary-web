package com.codecool;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by hamargyuri on 2017. 03. 20..
 */
public class MockAuthentication implements Authentication {
    private Collection<GrantedAuthority> authorities;

    public MockAuthentication(HashSet<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public MockAuthentication() {
        super();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> givenAuthorities = new HashSet<>();
        givenAuthorities.add(new SimpleGrantedAuthority("USER"));
        if (!(this.authorities == null)) {
            givenAuthorities.addAll(this.authorities);
        }
        return givenAuthorities;

    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}
