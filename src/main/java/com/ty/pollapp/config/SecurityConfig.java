package com.ty.pollapp.config;

import com.ty.pollapp.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Allow public access to static resources, login, and registration pages
                .requestMatchers("/css/**", "/js/**", "/images/**", "/login", "/register", "/").permitAll()
                
                // 🌟 CRITICAL SWAGGER FIX: Allow access to OpenAPI/Swagger UI endpoints
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Define role requirements for creating polls
                .requestMatchers("/polls/create").hasRole("ADMIN")
                
                // All other requests must be authenticated (logged in)
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")         // Use your custom login page
                .defaultSuccessUrl("/polls", true) 
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout") // Redirect here after successful logout
                .permitAll()
            )
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedHandler(accessDeniedHandler())
            );

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.sendRedirect("/polls?accessDenied");
        };
    }
}