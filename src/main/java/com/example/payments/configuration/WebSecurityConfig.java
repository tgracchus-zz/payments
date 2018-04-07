package com.example.payments.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().authorizeRequests().anyRequest().permitAll();

        http.cors().configurationSource(configurationSource -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Collections.singletonList(CorsConfiguration.ALL));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE", "HEAD"));
            return configuration;
        });

        //Content Security Policy (CSP)
        http
                .headers()
                .contentSecurityPolicy("script-src 'self';")
                .reportOnly();

        //X-Frame-Options
        http.headers()
                .frameOptions()
                .sameOrigin();

        //X-Content-Type-Options
        http.headers()
                .defaultsDisabled()
                .contentTypeOptions();

        //Referrer policy
        http.headers()
                .addHeaderWriter
                        (new StaticHeadersWriter("Referrer-Policy", "same-origin"));

        //X-XSS-Protection
        http.headers()
                .xssProtection()
                .block(true);

    }


}


