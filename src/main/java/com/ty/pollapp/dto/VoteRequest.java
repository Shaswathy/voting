package com.ty.pollapp.dto;

import jakarta.validation.constraints.NotNull;

public class VoteRequest {
    
    @NotNull(message = "You must select an option to vote.")
    private Long selectedOptionId;

    // NEW: Needed for tracking which poll the user is voting on
    @NotNull
    private Long pollId; 

    // --- Getters and Setters ---

    public Long getSelectedOptionId() { 
        return selectedOptionId; 
    }
    
    public void setSelectedOptionId(Long selectedOptionId) { 
        this.selectedOptionId = selectedOptionId; 
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }
}