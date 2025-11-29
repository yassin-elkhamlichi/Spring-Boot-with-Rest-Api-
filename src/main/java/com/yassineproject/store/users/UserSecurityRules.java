package com.yassineproject.store.users;

import com.yassineproject.store.auth.Role;
import com.yassineproject.store.common.SecurityRules;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;


@Component
public class UserSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
            registry.requestMatchers(HttpMethod.POST,"/users/**").permitAll();
            registry.requestMatchers(HttpMethod.GET,"/users/**").hasRole(Role.ADMIN.name());
            registry.requestMatchers(HttpMethod.DELETE,"/users/**").hasRole(Role.ADMIN.name());
            registry.requestMatchers(HttpMethod.PUT,"/users/**").hasRole(Role.ADMIN.name());
    }
}
