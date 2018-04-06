package com.example.payments.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;


@Configuration
public class WebSecurityConfig {


    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(
            ServerHttpSecurity http) {

        //X-Frame-Options
        http.headers()
                .frameOptions().mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN);

        http.csrf().disable();

        http.headers().cache().disable();

        return http.authorizeExchange()
                .anyExchange()
                .permitAll()
                .and()
                .build();
    }

    @Bean
    CorsConfiguration corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(CorsConfiguration.ALL));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE", "HEAD"));
        return configuration;
    }

}
