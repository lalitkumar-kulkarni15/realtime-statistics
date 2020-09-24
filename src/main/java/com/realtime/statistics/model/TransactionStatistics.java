package com.realtime.statistics.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TransactionStatistics {

    public TransactionStatistics() {
        reset();
    }

    private double avg;

    @JsonIgnore
    private double sum;

    private double max;

    private double min;

    private long count;

    @JsonIgnore
    private long timestamp;

    public void reset() {
        this.sum = 0;
        this.avg = 0;
        this.max = 0;
        this.min = 0;
        this.count = 0;
    }

}

