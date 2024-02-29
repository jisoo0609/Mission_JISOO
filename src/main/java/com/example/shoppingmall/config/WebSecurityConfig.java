package com.example.shoppingmall.config;

import com.example.shoppingmall.jwt.JwtTokenFilter;
import com.example.shoppingmall.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(
                                        "/token/issue",
                                        "/token/validate",
                                        "/users/register",
                                        "/users/login"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/users/{id}/update",
                                        "/users/{id}/request")
                                .authenticated()

                                .requestMatchers(
                                        "/admin/list",
                                        "/admin/check",
                                        "/admin/**")
                                .hasAuthority("ROLE_ADMIN")

                                .requestMatchers(
                                        "/used/**",
                                        "/used/create")
                                .hasAnyAuthority("ROLE_USER","ROLE_BUSINESS_USER","ROLE_ADMIN")

                                .requestMatchers(
                                        "/proposal/**",
                                        "/proposal/{id}")
                                .hasAnyAuthority("ROLE_USER","ROLE_BUSINESS_USER","ROLE_ADMIN")

                                .requestMatchers(
                                        "/shop/create",
                                        "/shop/{id}/**")
                                .hasAnyAuthority("ROLE_BUSINESS_USER","ROLE_ADMIN")

                                .requestMatchers(
                                        "/shop/admin/**",
                                        "/shop/admin/{id}"
                                )
                                .hasAuthority("ROLE_ADMIN")
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenUtils, manager),
                        AuthorizationFilter.class)
        ;
        return http.build();
    }
}
