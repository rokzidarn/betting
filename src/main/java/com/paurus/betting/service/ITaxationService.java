package com.paurus.betting.service;

import com.paurus.betting.json.Incoming;
import com.paurus.betting.json.Outgoing;

public interface ITaxationService {
    Outgoing generalByRate(Incoming data, String trader);
    Outgoing generalByAmount(Incoming data, String trader);
    Outgoing winningsByRate(Incoming data, String trader);
    Outgoing winningsByAmount(Incoming data, String trader);
}
