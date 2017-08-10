package com.n26.assignment.transactionstats.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Statistics {
    @JsonIgnore
    private long timestamp;
    private double sum;
    private double avg;
    private double min;
    private double max;
    private long count;
}
