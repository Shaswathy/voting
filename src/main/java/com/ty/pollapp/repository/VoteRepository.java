package com.ty.pollapp.repository;

import com.ty.pollapp.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    
    // CRITICAL: Checks if a vote already exists for the given user and poll
    Optional<Vote> findByUserIdAndPollId(Long userId, Long pollId);
}