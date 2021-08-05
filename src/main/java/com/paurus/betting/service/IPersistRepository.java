package com.paurus.betting.service;

import com.paurus.betting.model.MatchData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPersistRepository extends JpaRepository<MatchData, Long> {
}