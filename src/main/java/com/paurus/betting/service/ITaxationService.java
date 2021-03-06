package com.paurus.betting.service;

import com.paurus.betting.json.Incoming;
import com.paurus.betting.json.Outgoing;
import com.paurus.betting.model.Taxation;

public interface ITaxationService {
    Outgoing generalByRate(Incoming data);
    Outgoing generalByAmount(Incoming data);
    Outgoing general(Incoming data, String type);
    Outgoing winningsByRate(Incoming data);
    Outgoing winningsByAmount(Incoming data);
    Outgoing winnings(Incoming data, String type);
    Taxation createEntity(Incoming request, Outgoing response);
}
