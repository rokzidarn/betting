package com.paurus.betting.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "taxations")
@Builder
public class Taxation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "trader_id")
    private int traderId;

    @Column(name = "played_amount")
    private double playedAmount;

    @Column(name = "odd")
    @NonNull
    private double odd;

    @Column(name = "created")
    private Date created;

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    @Column(name = "possible_return_amount")
    private double possibleReturnAmount;

    @Column(name = "possible_return_amount_bef_tax")
    private double possibleReturnAmountBefTax;

    @Column(name = "possible_return_amount_after_tax")
    private double possibleReturnAmountAfterTax;

    @Column(name = "tax_rate")
    @Builder.Default
    private double taxRate = 0.0;

    @Column(name = "tax_amount")
    @Builder.Default
    private double taxAmount = 0.0;
}