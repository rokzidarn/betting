package com.paurus.betting.service;

import com.paurus.betting.model.MatchData;

public interface IPersistService {
    MatchData createEntity(String[] split);
    void trigger_baseline();
    void trigger_batch();
    void trigger_dll();
}
