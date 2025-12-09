package com.ty.pollapp.controller;

import com.ty.pollapp.dto.PollRequest;
import com.ty.pollapp.entity.Poll;
import com.ty.pollapp.entity.User;
import com.ty.pollapp.service.PollService;
import com.ty.pollapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/polls")
public class PollController {

    @Autowired
    private PollService pollService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listPolls(Model model) {
        model.addAttribute("polls", pollService.getAllPolls());
        return "polls/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("pollRequest", new PollRequest());
        return "polls/create";
    }

    @PostMapping("/save")
    public String savePoll(@ModelAttribute PollRequest pollRequest,
                           Authentication auth,
                           Model model) {

        // Validation
        if (pollRequest.getQuestion() == null || pollRequest.getQuestion().trim().isEmpty()) {
            model.addAttribute("error", "Question cannot be empty");
            return "polls/create";
        }

        if (pollRequest.getOptions() == null || pollRequest.getOptions().size() < 2) {
            model.addAttribute("error", "At least 2 options required");
            return "polls/create";
        }

        if (pollRequest.getExpiryDate() != null &&
                pollRequest.getExpiryDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Expiry date must be in the future");
            return "polls/create";
        }

        User creator = userService.getUserByUsername(auth.getName());

        pollService.createPoll(
                pollRequest.getQuestion(),
                pollRequest.getOptions(),
                pollRequest.getExpiryDate(),
                creator
        );

        return "redirect:/polls";
    }

    @PostMapping("/vote")
    public String vote(@RequestParam Long pollId,
                       @RequestParam Long optionId) {

        pollService.vote(pollId, optionId);
        return "redirect:/polls/results/" + pollId;
    }

    @GetMapping("/results/{id}")
    public String pollResults(@PathVariable Long id, Model model) {
        Poll poll = pollService.getPollById(id);
        model.addAttribute("poll", poll);
        return "polls/results";
    }
}

