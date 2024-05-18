package com.ecom.userservice.configurations;

//@Configuration
//public class WebSecurityConfig {

    /*
    These filters are needed when implementing spring security without JWT tokens and using UserController
    .generateToken()
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
//}
