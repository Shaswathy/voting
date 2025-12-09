package com.ty.pollapp.service;

import com.ty.pollapp.entity.Option;
import com.ty.pollapp.entity.Poll;
import com.ty.pollapp.entity.User;
import com.ty.pollapp.repository.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    @Transactional
    public Poll createPoll(String question, List<String> optionTexts, LocalDateTime expiryDate, User creator) {

        Poll poll = new Poll();
        poll.setQuestion(question);
        poll.setCreatedAt(LocalDateTime.now());
        poll.setExpiryDate(expiryDate);
        poll.setUser(creator);

        List<Option> options = optionTexts.stream().map(text -> {
            Option option = new Option();
            option.setText(text);
            option.setVotes(0);
            option.setPoll(poll);
            return option;
        }).collect(Collectors.toList());

        poll.setOptions(options);

        return pollRepository.save(poll);
    }

    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    public Poll getPollById(Long id) {
        return pollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Poll not found"));
    }

    @Transactional
    public void vote(Long pollId, Long optionId) {
        Poll poll = getPollById(pollId);

        if (poll.getExpiryDate() != null && poll.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Poll has expired!");
        }

        Option option = poll.getOptions().stream()
                .filter(o -> o.getId().equals(optionId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Option not found"));

        option.setVotes(option.getVotes() + 1);

        pollRepository.save(poll);
    }
}



