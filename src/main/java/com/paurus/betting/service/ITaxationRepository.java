package com.paurus.betting.service;

import com.paurus.betting.model.Taxation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaxationRepository extends JpaRepository<Taxation, Long> {
    List<Taxation> findByTraderId(int id);
}