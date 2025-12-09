package com.ty.pollapp.service;

import com.ty.pollapp.entity.User;
import com.ty.pollapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // --- ADMIN USER ---
        if (!userRepository.existsByUsername("admin")) {
            
            System.out.println("--- Creating default 'admin' user ---");
            
            User adminUser = new User();
            adminUser.setUsername("admin");
            
            adminUser.setPassword(passwordEncoder.encode("adminpass")); 
            
            // 🌟 CRITICAL FIX: Use setRole(String) and Spring Security prefix
            adminUser.setRole("ROLE_ADMIN"); 
            
            userRepository.save(adminUser);
            
            System.out.println("Default admin user created successfully! Username: admin, Password: adminpass");
        }
        
        // --- REGULAR USER ---
        if (!userRepository.existsByUsername("user")) {
            User regularUser = new User();
            regularUser.setUsername("user");
            regularUser.setPassword(passwordEncoder.encode("userpass"));
            
            // 🌟 CRITICAL FIX: Use setRole(String) and Spring Security prefix
            regularUser.setRole("ROLE_USER");
            
            userRepository.save(regularUser);
            System.out.println("Default regular user created. Username: user, Password: userpass");
        }
    }
}