package com.codecool;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

public class AnonymousAuthMock extends AnonymousAuthenticationToken {
    private static String key = "key";
    private static String principal = "principal";

    private AnonymousAuthMock(String key, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(key, principal, authorities);
    }

    public static AnonymousAuthMock withAddedAuth(HashSet<GrantedAuthority> authorities) {
        return new AnonymousAuthMock(key, principal, authorities);
    }

    public static AnonymousAuthMock withDefaultAuth() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return new AnonymousAuthMock(key, principal, authorities);
    }
}
