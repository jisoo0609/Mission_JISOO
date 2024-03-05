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

                                .requestMatchers("/users/{id}/update")
                                .authenticated()

                                .requestMatchers(
                                        "/users/{id}/request",
                                        "/users/{id}/**")
                                .hasAnyAuthority("ROLE_USER","ROLE_BUSINESS_USER","ROLE_ADMIN")

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
                                        "/shop/{id}/update",
                                        "/shop/{id}/submit",
                                        "/shop/{id}/close-request")
                                .hasAnyAuthority("ROLE_BUSINESS_USER","ROLE_ADMIN")

                                .requestMatchers(
                                        "/shop/admin/**",
                                        "/shop/admin/{id}"
                                )
                                .hasAuthority("ROLE_ADMIN")

                                .requestMatchers("/shop/{id}/management/**")
                                .hasAuthority("ROLE_BUSINESS_USER")

                                .requestMatchers(
                                        "/search/**",
                                        "/search/shop/{shopId}/product/**",
                                        "/search/shop/{shopId}/product/{productId}"
                                )
                                .hasAnyAuthority("ROLE_USER","ROLE_BUSINESS_USER","ROLE_ADMIN")

                                .requestMatchers(
                                        "/order/shop/{shopId}/product/{productId}/order/create",
                                        "/order/{orderId}/cancel"
                                ).hasAnyAuthority("ROLE_USER","ROLE_BUSINESS_USER","ROLE_ADMIN")

                                .requestMatchers(
                                        "/order/shop/{shopId}/product/{productId}/order/{orderId}/accept"
                                ).hasAuthority("ROLE_BUSINESS_USER")

                                .requestMatchers(
                                        "/toss/**"
                                ).permitAll()
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
