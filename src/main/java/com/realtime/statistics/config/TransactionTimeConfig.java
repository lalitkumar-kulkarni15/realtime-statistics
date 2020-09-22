package com.realtime.statistics.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "transaction")
public class TransactionTimeConfig {

    private int maxTimeAllowed;

    private int timeSamplingInterval;

}
