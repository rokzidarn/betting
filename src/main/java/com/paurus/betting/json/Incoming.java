package com.paurus.betting.json;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Incoming implements Serializable {
    private int traderId;
    private double playedAmount;
    private double odd;
}
