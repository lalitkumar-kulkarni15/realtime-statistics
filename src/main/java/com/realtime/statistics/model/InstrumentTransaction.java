package com.realtime.statistics.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InstrumentTransaction {

    @NotNull(message = "Instrument is a mandatory field")
    private String instrument;

    private double price;

    private long timestamp;

}
