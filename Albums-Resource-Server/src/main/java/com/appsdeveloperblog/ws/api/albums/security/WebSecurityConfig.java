/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appsdeveloperblog.ws.api.albums.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig  {

    @Bean
    SecurityFilterChain configure(HttpSecurity security) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        security.authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.GET, "/albums")
                        .hasRole("developer")
//                        .hasAuthority("SCOPE_profile")
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}).jwt().jwtAuthenticationConverter(jwtAuthenticationConverter));
        security.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return security.build();
    }

}
