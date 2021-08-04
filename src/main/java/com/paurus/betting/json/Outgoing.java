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
    private double taxRate;
    private double taxAmount;
    private String error;
}
