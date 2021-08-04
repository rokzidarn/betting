package com.paurus.betting.service;

import com.paurus.betting.json.Incoming;
import com.paurus.betting.json.Outgoing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class TaxationService implements ITaxationService{

    @Value("#{${taxation.traders.rate}}")
    private Map<String, Double> rates;

    @Value("#{${taxation.traders.amount}}")
    private Map<String, Double> amounts;

    @Override
    public Outgoing generalByRate(Incoming data, String trader) {
        return null;
    }

    @Override
    public Outgoing generalByAmount(Incoming data, String trader) {
        return null;
    }

    @Override
    public Outgoing winningsByRate(Incoming data, String trader) {
        return null;
    }

    @Override
    public Outgoing winningsByAmount(Incoming data, String trader) {
        return null;
    }
}
