package com.paurus.betting.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "match_data")
@Builder
public class MatchData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "market_id")
    private Integer marketId;

    @Column(name = "outcome_id")
    private String outcomeId;

    @Column(name = "specifiers")
    private String specifiers;
}
