package com.otus.order.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class AuthToken extends AbstractAuthenticationToken {

    private final String email;

    public AuthToken(String email) {
        super(null);
        this.email = email;
    }

    @Override
    public Object getCredentials() {
        return email;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }
}