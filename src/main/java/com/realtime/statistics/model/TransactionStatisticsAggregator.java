package com.realtime.statistics.model;

import lombok.Getter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Getter
public class TransactionStatisticsAggregator {

    private ReadWriteLock lock;

    private TransactionStatistics transactionStatistics;

    private Map<String, TransactionStatistics> transactionStatisticsMap;

    private long timestamp;

    public TransactionStatisticsAggregator() {

        this.lock = new ReentrantReadWriteLock();
        transactionStatistics = new TransactionStatistics();
        transactionStatisticsMap = new ConcurrentHashMap<>();
    }

    public void createTransactionStatistics(InstrumentTransaction instrumentTransaction) {

    	// Setting statistics for calculating total aggregated values.
        this.transactionStatistics.setMin(instrumentTransaction.getPrice());
        this.transactionStatistics.setMax(instrumentTransaction.getPrice());
        this.transactionStatistics.setCount(1);
        this.transactionStatistics.setSum(instrumentTransaction.getPrice());
        this.transactionStatistics.setAvg(instrumentTransaction.getPrice());
        this.transactionStatistics.setTimestamp(instrumentTransaction.getTimestamp());
        this.timestamp = instrumentTransaction.getTimestamp();

		// Setting statistics of instrument received for calculating total aggregated values specific to instrument.
        transactionStatisticsMap.put(instrumentTransaction.getInstrument(), new TransactionStatistics(instrumentTransaction.getPrice(),
                instrumentTransaction.getPrice(), instrumentTransaction.getPrice(), instrumentTransaction.getPrice(), 1, instrumentTransaction.getTimestamp()));

    }


    public void mergeToResult(TransactionStatistics result, int aggregatorRecordCounter) {

        try {

            getLock().readLock().lock();

            result.setSum(result.getSum() + getTransactionStatistics().getSum());
            result.setCount(result.getCount() + getTransactionStatistics().getCount());
            result.setAvg(result.getSum() / result.getCount());

            setMinPrice(result, aggregatorRecordCounter);

            if (result.getMax() < getTransactionStatistics().getMax()) {
                result.setMax(getTransactionStatistics().getMax());
            }

        } finally {
            getLock().readLock().unlock();
        }

    }

    private void setMinPrice(TransactionStatistics result, int aggregatorRecordCounter) {
        if (1 == aggregatorRecordCounter || result.getMin() > getTransactionStatistics().getMin()) {
            result.setMin(getTransactionStatistics().getMin());
        }
    }

    public void mergeStatistics(final InstrumentTransaction instrumentTransaction) {

        transactionStatistics.setSum(transactionStatistics.getSum() + instrumentTransaction.getPrice());
        transactionStatistics.setCount(transactionStatistics.getCount() + 1);
        transactionStatistics.setAvg(transactionStatistics.getSum() / transactionStatistics.getCount());

        transactionStatistics.setTimestamp(instrumentTransaction.getTimestamp());

        if (transactionStatistics.getMin() > instrumentTransaction.getPrice()) {
            transactionStatistics.setMin(instrumentTransaction.getPrice());
        }

        if (transactionStatistics.getMax() < instrumentTransaction.getPrice()) {
            transactionStatistics.setMax(instrumentTransaction.getPrice());
        }

		mergeInstrumentSpecificStatistics(instrumentTransaction);

    }

	private void mergeInstrumentSpecificStatistics(InstrumentTransaction instrumentTransaction) {

    	// Check if the statistics are already present of the input instrument
    	if (transactionStatisticsMap.containsKey(instrumentTransaction.getInstrument())) {

    		TransactionStatistics existingTransactionStatistics = transactionStatisticsMap.get(instrumentTransaction.getInstrument());
			existingTransactionStatistics.setSum(existingTransactionStatistics.getSum() + instrumentTransaction.getPrice());
			existingTransactionStatistics.setCount(existingTransactionStatistics.getCount() + 1);
			existingTransactionStatistics.setAvg(existingTransactionStatistics.getSum() / existingTransactionStatistics.getCount());
			existingTransactionStatistics.setTimestamp(instrumentTransaction.getTimestamp());

			if (existingTransactionStatistics.getMin() > instrumentTransaction.getPrice()) {
				existingTransactionStatistics.setMin(instrumentTransaction.getPrice());
			}

			if (existingTransactionStatistics.getMax() < instrumentTransaction.getPrice()) {
				existingTransactionStatistics.setMax(instrumentTransaction.getPrice());
			}

			transactionStatisticsMap.put(instrumentTransaction.getInstrument(), existingTransactionStatistics);
		} else {
    		// If not already present then add.
			transactionStatisticsMap.put(instrumentTransaction.getInstrument(), new TransactionStatistics(instrumentTransaction.getPrice(),
					instrumentTransaction.getPrice(), instrumentTransaction.getPrice(), instrumentTransaction.getPrice(), 1, instrumentTransaction.getTimestamp()));
		}
	}

	public void reset() {
        transactionStatistics.reset();
        timestamp = 0;
    }

    public boolean isEmpty() {
        return transactionStatistics.getCount() == 0;
    }


}
