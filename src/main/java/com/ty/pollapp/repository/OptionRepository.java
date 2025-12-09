package com.ty.pollapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ty.pollapp.entity.Option;

public interface OptionRepository extends JpaRepository<Option, Long> {
    // Custom method to find an Option with its Poll loaded (if needed)
    // Optional<Option> findById(Long id); 
}