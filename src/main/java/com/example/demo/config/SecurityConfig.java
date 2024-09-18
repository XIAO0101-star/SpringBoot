package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// @Configuration marks the class a configuration for some aspect of Springboot
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // A @Bean method returns a component (aka "bean") that is used by Spring Boot
    // Think of it as a "factory method" that Spring Boot will use later, sometimes internally.
    // to create an instance of a component that it needs
    // If Spring Boot ever needs an object of the class `SecurityFilterChain`, it will call this method

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // the `httpSecurity` class allows us to define security rules
        http
        .csrf( csrf -> csrf.ignoringRequestMatchers("/stripe/webhook"))
        .authorizeHttpRequests(authz -> 
            // all of these URLs can be accessed without login
            authz
            .requestMatchers("/register", "/login", "/css/**", "/js/**", "/stripe/webhook").permitAll() 
            .anyRequest().authenticated()  // switch to .permitAll() to allow access by non-authenticated users
           
        )
        .formLogin(form -> 
            form.loginPage("/login")
            .permitAll()
        )
        .logout(logout -> logout.permitAll());
        return http.build();
    }  
}
