package com.ty.pollapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public class PollRequest {

    @NotEmpty(message = "Question cannot be empty.")
    @Size(min = 5, message = "Question must be at least 5 characters.")
    private String question;

    // We use @Valid to ensure validation runs on the nested OptionRequest list
    @Valid
    @Size(min = 2, max = 10, message = "A poll must have between 2 and 10 options.")
    private List<OptionRequest> optionRequests;

    // Getters and Setters...
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public List<OptionRequest> getOptionRequests() { return optionRequests; }
    public void setOptionRequests(List<OptionRequest> optionRequests) { this.optionRequests = optionRequests; }
}