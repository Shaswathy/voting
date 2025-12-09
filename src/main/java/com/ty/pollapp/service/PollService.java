package com.ty.pollapp.service;

import com.ty.pollapp.dto.PollRequest;
import com.ty.pollapp.entity.Option;
import com.ty.pollapp.entity.Poll;
import com.ty.pollapp.entity.User;
import com.ty.pollapp.entity.Vote;
import com.ty.pollapp.repository.OptionRepository;
import com.ty.pollapp.repository.PollRepository;
import com.ty.pollapp.repository.UserRepository; // NEW: Required to fetch the User entity
import com.ty.pollapp.repository.VoteRepository; // NEW: Required for vote tracking
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollService {

    private final PollRepository pollRepository;
    private final OptionRepository optionRepository;
    private final VoteRepository voteRepository; // NEW
    private final UserRepository userRepository; // NEW

    // UPDATED Constructor
    public PollService(PollRepository pollRepository, OptionRepository optionRepository, 
                       VoteRepository voteRepository, UserRepository userRepository) {
        this.pollRepository = pollRepository;
        this.optionRepository = optionRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    // --- Poll Creation (Remains the same) ---
    @Transactional
    public Poll createNewPoll(PollRequest pollRequest) {
        // ... (Existing code for createNewPoll)
        Poll poll = new Poll();
        poll.setQuestion(pollRequest.getQuestion());

        List<Option> options = pollRequest.getOptionRequests().stream()
                .map(optionReq -> {
                    Option option = new Option();
                    option.setText(optionReq.getText());
                    option.setPoll(poll);
                    return option;
                })
                .collect(Collectors.toList());

        poll.setOptions(options);

        return pollRepository.save(poll);
    }
    
    // --- Vote Recording Logic (MODIFIED) ---
    @Transactional
    public void recordVote(Long userId, Long pollId, Long optionId) {
        
        // 1. CRITICAL: Check if the user has already voted on this poll
        if (voteRepository.findByUserIdAndPollId(userId, pollId).isPresent()) {
            throw new RuntimeException("ALREADY_VOTED"); // Use a specific error string
        }

        // 2. Increment the vote count on the Option
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found with ID: " + optionId));
        
        option.incrementVoteCount();
        optionRepository.save(option);
        
        // 3. Create the Vote tracking record
        Poll poll = findPollById(pollId); // Reuse existing method
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
            
        Vote vote = new Vote();
        vote.setUser(user);
        vote.setPoll(poll);
        vote.setOption(option);
        voteRepository.save(vote);
    }
    
    // --- Retrieval Methods (Remains the same) ---

    public List<Poll> findAllPolls() {
        return pollRepository.findAll();
    }
    
    public Poll findPollById(Long id) {
        return pollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Poll not found with ID: " + id));
    }
    
    // NEW: Method to check if user has already voted (for Thymeleaf display)
    @Transactional(readOnly = true)
    public boolean hasUserVoted(Long userId, Long pollId) {
        return voteRepository.findByUserIdAndPollId(userId, pollId).isPresent();
    }
}