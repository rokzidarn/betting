package com.paurus.betting.service;

import com.paurus.betting.model.MatchData;

import java.io.IOException;

public interface IPersistService {
    MatchData createEntity(String[] split);
    void trigger_baseline();
    void trigger_batch();
    void trigger_dll();
    void trigger_rank();
    void trigger_thread();
    void trigger_final();
    String rank(String prevRank, String nextRank);
}
