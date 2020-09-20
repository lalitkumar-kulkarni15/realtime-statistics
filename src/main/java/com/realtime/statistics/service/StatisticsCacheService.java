package com.realtime.statistics.service;

import com.realtime.statistics.model.InstrumentTransaction;
import com.realtime.statistics.model.TransactionStatistics;

public interface StatisticsCacheService {

    void addTransaction(InstrumentTransaction instrumentTransaction);

    TransactionStatistics getTransactionStatistics();

}
