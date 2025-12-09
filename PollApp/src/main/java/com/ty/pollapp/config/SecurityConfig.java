package com.ty.pollapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for simplicity (consider enabling in production)
            .csrf(csrf -> csrf.disable())
            
            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/polls/create", "/polls/save").hasRole("ADMIN") // Admin only
                .requestMatchers("/polls/vote", "/polls/results/**").hasAnyRole("USER", "ADMIN") // Voting and results accessible to both
                .requestMatchers("/polls", "/auth/**", "/css/**", "/js/**").permitAll() // Public pages
                .anyRequest().authenticated()
            )
            
            // Login configuration
            .formLogin(form -> form
                .loginPage("/auth/login")
                .defaultSuccessUrl("/polls", true)
                .permitAll()
            )
            
            // Logout configuration
            .logout(logout -> logout
                .logoutSuccessUrl("/auth/login")
                .permitAll()
            );

        return http.build();
    }
}




