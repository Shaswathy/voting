package com.ty.pollapp.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PollRequest {
    private String question;
    private List<String> options;
    private LocalDateTime expiryDate;

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
}
