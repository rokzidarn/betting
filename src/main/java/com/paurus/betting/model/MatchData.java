package com.paurus.betting.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "match_data")
@Builder
public class MatchData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)  // because of batch operations
    private long id;
    // TODO: remove in case of speeding up, now is up to Postgres, UUID.randomUUID();

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "market_id")
    private Integer marketId;

    @Column(name = "outcome_id")
    private String outcomeId;

    @Column(name = "specifiers")
    private String specifiers;

    @Column(name = "date_insert")
    private Date date_insert;

    @PrePersist
    protected void onInsert() {
        date_insert = new Date();
    }

    @Column(name = "rank", length = 3000)
    private String rank;

    @Column(name = "next")
    private String next;

    @Column(name = "prev")
    private String prev;
}
