package com.ty.pollapp.controller;

import com.ty.pollapp.entity.Poll;
import com.ty.pollapp.service.PollService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Dedicated REST Controller for API Documentation via Swagger/OpenAPI.
 * Uses @RestController to ensure endpoints are documented.
 */
@RestController
@RequestMapping("/api/v1/polls")
public class PollApiController {

    private final PollService pollService;

    public PollApiController(PollService pollService) {
        this.pollService = pollService;
    }

    @GetMapping
    public List<Poll> getAllPolls() {
        // Find all polls and return them as JSON
        return pollService.findAllPolls();
    }

    @GetMapping("/{pollId}")
    public Poll getPollById(@PathVariable Long pollId) {
        // Find a specific poll by ID and return it as JSON
        return pollService.findPollById(pollId);
    }
}