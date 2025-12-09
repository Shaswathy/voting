package com.ty.pollapp.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ty.pollapp.dto.UserRequest;
import com.ty.pollapp.entity.User;
import com.ty.pollapp.repository.UserRepository;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final UserRepository userRepository; // To check existence
    private final PasswordEncoder passwordEncoder; // To encode the password

    // 🌟 Ensure both dependencies are injected
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- Show Registration Form ---
    @GetMapping("/register")
    public String showRegistrationForm(@ModelAttribute("userRequest") UserRequest userRequest) {
        return "auth/register";
    }

    // --- Handle Registration Submission (CORRECTED METHOD) ---
    @PostMapping("/register")
    public String registerNewUser(@Valid @ModelAttribute("userRequest") UserRequest userRequest,
                                BindingResult result) {
        
        // 1. Handle Validation Errors
        if (result.hasErrors()) {
            // Returns to the form to display field-specific errors
            return "auth/register"; 
        }

        // 2. Handle Duplicate Username
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            // Add a global error that Thymeleaf can display
            result.rejectValue("username", "user.username.exists", "Username is already taken.");
            return "auth/register"; 
        }

        // 3. Create and Save the New User
        User user = new User();
        user.setUsername(userRequest.getUsername());
        
        // CRITICAL: Encode the password before saving
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        
        // Default role for new users is ROLE_USER
        user.setRole("ROLE_USER"); 

        userRepository.save(user);

        // Redirect to login page upon success
        return "redirect:/login?registered"; 
    }

    // --- Login Mapping (Remains the same) ---
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }
}