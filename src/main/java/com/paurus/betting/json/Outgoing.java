package com.paurus.betting.json;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Outgoing implements Serializable {
    private double possibleReturnAmount;
    private double possibleReturnAmountBefTax;
    private double possibleReturnAmountAfterTax;

    @Builder.Default
    private double taxRate = 0.0;

    @Builder.Default
    private double taxAmount = 0.0;

    @Builder.Default
    private String error = null;
}
