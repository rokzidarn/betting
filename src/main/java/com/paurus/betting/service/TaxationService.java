package com.paurus.betting.service;

import com.paurus.betting.json.Incoming;
import com.paurus.betting.json.Outgoing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaxationService implements ITaxationService{

    //@Value("${general.tax}")
    //private double tax;

    @Override
    public Outgoing generalByRate(Incoming data) {
        return null;
    }

    @Override
    public Outgoing generalByAmount(Incoming data) {
        return null;
    }

    @Override
    public Outgoing winningsByRate(Incoming data) {
        return null;
    }

    @Override
    public Outgoing winningsByAmount(Incoming data) {
        return null;
    }
}
