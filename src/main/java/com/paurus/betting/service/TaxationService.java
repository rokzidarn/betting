package com.paurus.betting.service;

import com.paurus.betting.json.Incoming;
import com.paurus.betting.json.Outgoing;
import com.paurus.betting.model.Taxation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class TaxationService implements ITaxationService{

    @Value("#{${taxation.traders.rate}}")
    private Map<Integer, Double> rates;

    @Value("#{${taxation.traders.amount}}")
    private Map<Integer, Double> amounts;

    @Deprecated
    @Override
    public Outgoing generalByRate(Incoming data) {
        Double taxRate = rates.get(data.getTraderId());
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

    @Deprecated
    @Override
    public Outgoing generalByAmount(Incoming data) {
        Double taxAmount = amounts.get(data.getTraderId());
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
    public Outgoing general(Incoming data, String type) {
        Double taxRate = type.equals("rate") ? rates.get(data.getTraderId()) : null;
        Double taxAmount = type.equals("amount") ? amounts.get(data.getTraderId()) : null;
        Outgoing.OutgoingBuilder response = Outgoing.builder();

        if (taxRate == null && taxAmount == null) {
            response.error("Tax rate/amount for this trader not found!");
        } else {
            double possibleReturnAmount = data.getPlayedAmount() * data.getOdd();  // 5.0 * 1.5 = 7.5
            double possibleReturnAmountBefTax = possibleReturnAmount * 1.0;
            double tax = taxRate != null ? possibleReturnAmount * taxRate : 0.0;  // 7.5 * 0.1 = 0.75
            double possibleReturnAmountAfterTax = ((taxAmount != null && possibleReturnAmount > taxAmount) || taxRate != null) ?
                    possibleReturnAmount - tax - (taxAmount != null ? taxAmount : 0.0) : 0.0;

            response.taxRate(taxRate != null ? taxRate : 0.0)
                    .taxAmount(taxAmount != null ? taxAmount : 0.0)
                    .possibleReturnAmount(possibleReturnAmount)
                    .possibleReturnAmountBefTax(possibleReturnAmountBefTax)
                    .possibleReturnAmountAfterTax(possibleReturnAmountAfterTax);
        }

        return response.build();
    }

    @Deprecated
    @Override
    public Outgoing winningsByRate(Incoming data) {
        Double taxRate = rates.get(data.getTraderId());
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

    @Deprecated
    @Override
    public Outgoing winningsByAmount(Incoming data) {
        Double taxAmount = amounts.get(data.getTraderId());
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

    @Override
    public Outgoing winnings(Incoming data, String type) {
        Double taxRate = type.equals("rate") ? rates.get(data.getTraderId()) : null;
        Double taxAmount = type.equals("amount") ? amounts.get(data.getTraderId()) : null;
        Outgoing.OutgoingBuilder response = Outgoing.builder();

        if (taxRate == null && taxAmount == null) {
            response.error("Tax rate/amount for this trader not found!");
        } else {
            double possibleReturnAmount = data.getPlayedAmount() * data.getOdd();  // 5.0 * 1.5 = 7.5
            double possibleReturnAmountBefTax = possibleReturnAmount * 1.0;
            double winnings = possibleReturnAmount - data.getPlayedAmount();  // 7.5 - 5.0 = 2.5
            double possibleReturnAmountAfterTax = 0.0;

            if (taxRate != null) {
                possibleReturnAmountAfterTax = possibleReturnAmount - (winnings * taxRate);  // 7.5 - (2.5 * 0.1) = 7.25
            } else { // taxAmount != null
                possibleReturnAmountAfterTax = winnings > taxAmount ? winnings - taxAmount : 0.0;  // 2.5 - 1.0 = 1.5
            }

            response.taxRate(taxRate != null ? taxRate : 0.0)
                    .taxAmount(taxAmount != null ? taxAmount : 0.0)
                    .possibleReturnAmount(possibleReturnAmount)
                    .possibleReturnAmountBefTax(possibleReturnAmountBefTax)
                    .possibleReturnAmountAfterTax(possibleReturnAmountAfterTax);
        }

        return response.build();
    }

    @Override
    public Taxation createEntity(Incoming request, Outgoing response) {
        return Taxation.builder()
                .traderId(request.getTraderId())
                .playedAmount(request.getPlayedAmount())
                .odd(request.getOdd())
                .taxRate(response.getTaxRate())
                .taxAmount(response.getTaxAmount())
                .possibleReturnAmount(response.getPossibleReturnAmount())
                .possibleReturnAmountBefTax(response.getPossibleReturnAmountBefTax())
                .possibleReturnAmountAfterTax(response.getPossibleReturnAmountAfterTax())
                .build();
    }
}
