package com.ty.pollapp.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PollResponse {
    private Long id;
    private String question;
    private LocalDateTime expiryDate;
    private List<OptionResponse> options;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }

    public List<OptionResponse> getOptions() { return options; }
    public void setOptions(List<OptionResponse> options) { this.options = options; }
}
