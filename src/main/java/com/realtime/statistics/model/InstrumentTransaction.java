package com.realtime.statistics.model;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class InstrumentTransaction {

    @NotNull(message = "Instrument is a mandatory field")
    private String instrument;

    private double price;

    private long timestamp;

}
