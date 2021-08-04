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

    // TODO: refactor, join method rate/amount

    @Override
    public Outgoing generalByRate(Incoming data, String trader) {
        Double taxRate = rates.get(trader);
        Outgoing.OutgoingBuilder response = Outgoing.builder();

        if (taxRate != null) {
            double possibleReturnAmount = data.getPlayedAmount() * data.getOdd();  // 5.0 * 1.5 = 7.5
            double tax = possibleReturnAmount * taxRate;  // 7.5 * 0.1 = 0.75
            double possibleReturnAmountBefTax = possibleReturnAmount * 1.0;  // TODO: what is difference before/after tax?
            double possibleReturnAmountAfterTax = possibleReturnAmount - tax;  // 7.5 - 0.75 = 6.75

            response.taxRate(taxRate)
                    .possibleReturnAmount(possibleReturnAmount)
                    .possibleReturnAmountBefTax(possibleReturnAmountBefTax)
                    .possibleReturnAmountAfterTax(possibleReturnAmountAfterTax);
        } else {
            response.error("Tax rate for this trader not found!");
        }

        return response.build();
    }

    @Override
    public Outgoing generalByAmount(Incoming data, String trader) {
        Double taxAmount = amounts.get(trader);
        Outgoing.OutgoingBuilder response = Outgoing.builder();

        if (taxAmount != null) {
            double possibleReturnAmount = data.getPlayedAmount() * data.getOdd();  // 5.0 * 1.5 = 7.5
            double possibleReturnAmountBefTax = possibleReturnAmount * 1.0;
            double possibleReturnAmountAfterTax = possibleReturnAmount > taxAmount ? possibleReturnAmount - taxAmount : 0.0;  // 7.5 - 2 = 5.5
            // TODO: what if tax amount larger than possible return?

            response.taxAmount(taxAmount)
                    .possibleReturnAmount(possibleReturnAmount)
                    .possibleReturnAmountBefTax(possibleReturnAmountBefTax)
                    .possibleReturnAmountAfterTax(possibleReturnAmountAfterTax);
        } else {
            response.error("Tax amount for this trader not found!");
        }

        return response.build();
    }

    @Override
    public Outgoing winningsByRate(Incoming data, String trader) {
        Double taxRate = rates.get(trader);
        Outgoing.OutgoingBuilder response = Outgoing.builder();

        if (taxRate != null) {
            double possibleReturnAmount = data.getPlayedAmount() * data.getOdd();  // 5.0 * 1.5 = 7.5
            double possibleReturnAmountBefTax = possibleReturnAmount * 1.0;
            double winnings = possibleReturnAmount - data.getPlayedAmount();  // 7.5 - 5.0 = 2.5
            double possibleReturnAmountAfterTax = possibleReturnAmount - (winnings * taxRate);  // 7.5 - (2.5 * 0.1) = 7.25

            response.taxRate(taxRate)
                    .possibleReturnAmount(possibleReturnAmount)
                    .possibleReturnAmountBefTax(possibleReturnAmountBefTax)
                    .possibleReturnAmountAfterTax(possibleReturnAmountAfterTax);
        } else {
            response.error("Tax rate for this trader not found!");
        }

        return response.build();
    }

    @Override
    public Outgoing winningsByAmount(Incoming data, String trader) {
        Double taxAmount = amounts.get(trader);
        Outgoing.OutgoingBuilder response = Outgoing.builder();

        if (taxAmount != null) {
            double possibleReturnAmount = data.getPlayedAmount() * data.getOdd();  // 5.0 * 1.5 = 7.5
            double possibleReturnAmountBefTax = possibleReturnAmount * 1.0;
            double winnings = possibleReturnAmount - data.getPlayedAmount();  // 7.5 - 5.0 = 2.5
            double possibleReturnAmountAfterTax = winnings > taxAmount ? winnings - taxAmount : 0.0;  // 2.5 - 1.0 = 1.5

            response.taxAmount(taxAmount)
                    .possibleReturnAmount(possibleReturnAmount)
                    .possibleReturnAmountBefTax(possibleReturnAmountBefTax)
                    .possibleReturnAmountAfterTax(possibleReturnAmountAfterTax);
        } else {
            response.error("Tax amount for this trader not found!");
        }

        return response.build();
    }
}
