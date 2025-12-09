package com.ty.pollapp.controller;

import com.ty.pollapp.dto.PollRequest;
import com.ty.pollapp.dto.VoteRequest;
import com.ty.pollapp.entity.Poll;
import com.ty.pollapp.entity.User;
import com.ty.pollapp.service.PollService;
import com.ty.pollapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.ty.pollapp.dto.OptionRequest;
import java.util.Arrays;

@Controller
@RequestMapping("/polls")
public class PollController {

    private final PollService pollService;
    private final UserService userService; 

    public PollController(PollService pollService, UserService userService) {
        this.pollService = pollService;
        this.userService = userService;
    }

    // --- List Polls (CRITICAL FIX: Changed from @GetMapping("/list") to @GetMapping) ---
    @GetMapping 
    public String listPolls(Model model) {
        model.addAttribute("polls", pollService.findAllPolls());
        return "polls/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        PollRequest pollRequest = new PollRequest();
        pollRequest.setOptionRequests(Arrays.asList(new OptionRequest(), new OptionRequest())); 
        model.addAttribute("pollRequest", pollRequest);
        return "polls/create";
    }

    @PostMapping("/create")
    public String createPoll(@Valid @ModelAttribute("pollRequest") PollRequest pollRequest,
                             BindingResult result) {
        if (result.hasErrors()) { return "polls/create"; }
        Poll newPoll = pollService.createNewPoll(pollRequest);
        return "redirect:/polls/" + newPoll.getId();
    }
    
    // --- View Poll and Voting Form ---
    // This mapping now correctly catches only numerical IDs
    @GetMapping("/{pollId}")
    public String viewPoll(@PathVariable Long pollId, Model model, 
                           @AuthenticationPrincipal UserDetails currentUser) {
        
        Poll poll = pollService.findPollById(pollId);
        model.addAttribute("poll", poll);
        
        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setPollId(pollId); 
        model.addAttribute("voteRequest", voteRequest); 

        boolean hasVoted = false;
        if (currentUser != null) {
            User user = userService.findByUsername(currentUser.getUsername());
            hasVoted = pollService.hasUserVoted(user.getId(), pollId);
        }
        model.addAttribute("hasVoted", hasVoted);
        
        return "polls/view";
    }

    // --- Handle Vote Submission ---
    @PostMapping("/{pollId}/vote")
    public String submitVote(@PathVariable Long pollId,
                             @Valid @ModelAttribute("voteRequest") VoteRequest voteRequest,
                             BindingResult result,
                             @AuthenticationPrincipal UserDetails currentUser) {
        
        if (result.hasErrors()) {
            return "redirect:/polls/" + pollId + "?error"; 
        }

        if (currentUser == null) { return "redirect:/login"; }
        
        User user = userService.findByUsername(currentUser.getUsername());
        
        try {
            pollService.recordVote(
                user.getId(), 
                pollId, 
                voteRequest.getSelectedOptionId()
            );
        } catch (RuntimeException e) {
            if ("ALREADY_VOTED".equals(e.getMessage())) {
                 return "redirect:/polls/" + pollId + "?alreadyVoted"; 
            }
            throw e; 
        }
        
        return "redirect:/polls/" + pollId + "/results"; 
    }

    // --- View Results ---
    @GetMapping("/{pollId}/results")
    public String viewResults(@PathVariable Long pollId, Model model) {
        Poll poll = pollService.findPollById(pollId);
        model.addAttribute("poll", poll);
        return "polls/results";
    }
}