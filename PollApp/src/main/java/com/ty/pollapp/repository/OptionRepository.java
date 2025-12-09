package com.ty.pollapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ty.pollapp.entity.Option;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
}

