package com.ecom.userservice.configurations;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    /*
    These filters were added when Spring security was not implemented fully, now after implementing JWT these filters
    are redundant as under pkg security these filters are created.
     */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((requests) -> {
//                    try {
//                        requests
//                                .anyRequest().permitAll()
//                                .and().cors().disable()
//                                .csrf().disable();
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//
//        return http.build();
//    }
}
