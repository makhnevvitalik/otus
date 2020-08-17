package com.otus.order;

import com.otus.order.auth.AuthFilter;
import com.otus.order.auth.AuthProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@ConditionalOnProperty(value = "auth.enabled", matchIfMissing = true)
@Configuration
@EnableWebSecurity
public class AuthConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new AuthProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new AuthFilter(), AnonymousAuthenticationFilter.class)
            .authorizeRequests().requestMatchers(new AntPathRequestMatcher("/v1/*")).authenticated().and()
            .cors().disable()
            .csrf().disable();
    }
}