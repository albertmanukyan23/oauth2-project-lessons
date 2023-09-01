package com.appsdeveloperblog.clients.sociallogin.socialloginwebclient.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

    private  final ClientRegistrationRepository clientRegistrationRepository;
    @Bean
    SecurityFilterChain configure(HttpSecurity security) throws Exception {

        security.authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .anyRequest()
                        .authenticated())
                .oauth2Login()
                .and()
                .logout()
//                .logoutSuccessUrl("/")
                .logoutSuccessHandler(oidcLogoutSuccessHandler())
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID");
        return security.build();
    }
    private OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler successHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri("http://localhost:8080/");
        return successHandler;
    }
}
