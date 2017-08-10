package com.n26.assignment.transactionstats.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transaction {
    private long timestamp;
    private double amount;
}
