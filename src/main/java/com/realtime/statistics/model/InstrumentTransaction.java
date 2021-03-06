package com.realtime.statistics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
public class InstrumentTransaction {

    @NotNull(message = "Instrument is a mandatory field")
    private String instrument;

    @Min(value = 0,message = "Price cannot be negative")
    private double price;

    private long timestamp;

}
