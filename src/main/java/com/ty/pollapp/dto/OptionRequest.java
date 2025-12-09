package com.ty.pollapp.dto;

import jakarta.validation.constraints.NotEmpty;

public class OptionRequest {
    
    @NotEmpty(message = "Option text cannot be empty.")
    private String text;

    // Getters and Setters...
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}