package com.otus.order.auth;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class AuthUtils {

    public String getUserLogin() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
