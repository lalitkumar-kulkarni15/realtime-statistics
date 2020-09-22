package com.realtime.statistics.service.impl;

import com.realtime.statistics.exception.TransactionOutOfRangeException;
import com.realtime.statistics.model.InstrumentTransaction;
import com.realtime.statistics.config.TransactionTimeConfig;
import com.realtime.statistics.model.TransactionStatistics;
import com.realtime.statistics.model.TransactionStatisticsAggregator;
import com.realtime.statistics.service.TransactionCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.time.Instant;

@Component
public class TransactionStatisticsCacheImpl implements TransactionCache {

    private TransactionStatisticsCacheImpl() {}

    @Autowired
    private TransactionTimeConfig transactionTimeConfig;

    private TransactionStatisticsAggregator[] transactionStatisticsAggregatorArr;

    @PostConstruct
    private void initialiseTransactionStatisticsAggregator() {

        this.transactionStatisticsAggregatorArr = new TransactionStatisticsAggregator[transactionTimeConfig.getMaxTimeAllowed() / transactionTimeConfig.getTimeSamplingInterval()];

        for (int arrayIndex = 0; arrayIndex < transactionStatisticsAggregatorArr.length; arrayIndex++) {
            transactionStatisticsAggregatorArr[arrayIndex] = new TransactionStatisticsAggregator();
        }
    }

    @Override
    public synchronized void addTransaction(InstrumentTransaction instrumentTransaction) {

        long currentTimestamp = Instant.now().toEpochMilli();
        validateTransaction(instrumentTransaction, currentTimestamp);
        aggregateTransactions(instrumentTransaction, currentTimestamp);
    }

    @Override
    public synchronized TransactionStatistics getTransactionStatistics() {

        long currentTimestamp = Instant.now().toEpochMilli();
        TransactionStatistics resultantStatistics = new TransactionStatistics();
        int aggregatorRecordCounter = 0;

        for (TransactionStatisticsAggregator transactionStatisticsAggregator : transactionStatisticsAggregatorArr) {

            if (isTransactionValid(transactionStatisticsAggregator.getTimestamp(), currentTimestamp, transactionTimeConfig.getMaxTimeAllowed())) {
                aggregatorRecordCounter++;
                transactionStatisticsAggregator.mergeToResult(resultantStatistics, aggregatorRecordCounter);
            }

        }

        return resultantStatistics;

    }

    @Override
    public synchronized TransactionStatistics getTransactionStatistics(final String instrument) {

        long currentTimestamp = Instant.now().toEpochMilli();
        TransactionStatistics resultantStatistics = new TransactionStatistics();
        int aggregatorRecordCounter = 0;

        for (TransactionStatisticsAggregator transactionStatisticsAgg : transactionStatisticsAggregatorArr) {

            if (transactionStatisticsAgg.getTransactionStatisticsMap().containsKey(instrument)) {

                TransactionStatistics transactionStatistics = transactionStatisticsAgg.getTransactionStatisticsMap().get(instrument);

                populateResultantStatistics(currentTimestamp, resultantStatistics, aggregatorRecordCounter, transactionStatisticsAgg, transactionStatistics);

            }
        }

        return resultantStatistics;
    }

    private void populateResultantStatistics(long currentTimestamp, TransactionStatistics resultantStatistics, int aggregatorRecordCounter, TransactionStatisticsAggregator transactionStatisticsAgg, TransactionStatistics transactionStatistics) {

        if (isTransactionValid(transactionStatistics.getTimestamp(), currentTimestamp, transactionTimeConfig.getMaxTimeAllowed())) {
            aggregatorRecordCounter++;
            transactionStatisticsAgg.mergeToResult(resultantStatistics, aggregatorRecordCounter);
        }

    }

    private void aggregateTransactions(InstrumentTransaction instrumentTransaction, long currentTimestamp) {

        final int index = getInstrumentTransactionIndex(instrumentTransaction, currentTimestamp);
        TransactionStatisticsAggregator txnStatisticAggregator = transactionStatisticsAggregatorArr[index];

        try {

            txnStatisticAggregator.getLock().writeLock().lock();
            // in case aggregator is empty
            if (txnStatisticAggregator.isEmpty()) {
                txnStatisticAggregator.createTransactionStatistics(instrumentTransaction);
            } else {
                // check if existing aggregator is still valid
                if (isTransactionValid(txnStatisticAggregator.getTimestamp(), currentTimestamp, transactionTimeConfig.getMaxTimeAllowed())) {
                    txnStatisticAggregator.mergeStatistics(instrumentTransaction);
                } else {
                    //if not valid
                    txnStatisticAggregator.reset();
                    txnStatisticAggregator.createTransactionStatistics(instrumentTransaction);
                }
            }
        } finally {
            txnStatisticAggregator.getLock().writeLock().unlock();
        }

    }

    private void validateTransaction(InstrumentTransaction instrumentTransaction, long currentTimestamp) {

        if (isTransactionOutOfRange(instrumentTransaction.getTimestamp(), currentTimestamp, transactionTimeConfig.getMaxTimeAllowed())) {
            throw new TransactionOutOfRangeException("The received transaction is out of range.");
        }
    }

    private int getInstrumentTransactionIndex(InstrumentTransaction instrumentTransaction, long currentTimestamp) {

        long txnTime = instrumentTransaction.getTimestamp();
        return (int) ((currentTimestamp - txnTime) / transactionTimeConfig.getTimeSamplingInterval()) % (transactionTimeConfig.getMaxTimeAllowed() / transactionTimeConfig.getTimeSamplingInterval());
    }

    private boolean isTransactionValid(long txnTimeStamp, long currentTimestamp, int maxTimeAllowed) {
        return !isTransactionOutOfRange(txnTimeStamp, currentTimestamp, maxTimeAllowed);
    }

    private boolean isTransactionOutOfRange(long txnTimeStamp, long currentTimestamp, int maxTimeAllowed) {
        return txnTimeStamp <= currentTimestamp - maxTimeAllowed;
    }


}
