package com.example.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConf {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF for testing with Postman
                .authorizeHttpRequests()
                .requestMatchers("/api/users/register").permitAll() // Allow POST without auth
                .anyRequest().authenticated() // Require auth for others
                .and()
                .httpBasic(); // You can use formLogin() if you're building a UI

        return http.build();
    }
}
