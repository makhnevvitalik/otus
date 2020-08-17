package com.otus.order.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class AuthProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        authentication.setAuthenticated(true);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(AuthToken.class);
    }
}
