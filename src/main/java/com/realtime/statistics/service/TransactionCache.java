package com.realtime.statistics.service;

import com.realtime.statistics.model.InstrumentTransaction;
import com.realtime.statistics.model.TransactionStatistics;

public interface TransactionCache {

    void addTransaction(InstrumentTransaction instrumentTransaction);

    TransactionStatistics getTransactionStatistics();

    TransactionStatistics getTransactionStatistics(final String instrument);

}
