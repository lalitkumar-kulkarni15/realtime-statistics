package com.realtime.statistics.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Map;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TransactionStatisticsAggregatorTest {

    @Test
    public void verifiesTransactionStatisticsKeyAfterAddingTransaction(){

        InstrumentTransaction instrumentTransaction = new InstrumentTransaction("IBM",200.00,98798798798L);
        TransactionStatisticsAggregator transactionStatisticsAggregator = new TransactionStatisticsAggregator();
        transactionStatisticsAggregator.createTransactionStatistics(instrumentTransaction);
        Map<String,TransactionStatistics> transactionStatisticsMap = transactionStatisticsAggregator.getTransactionStatisticsMap();
        assertNotNull(transactionStatisticsMap);
        assertTrue(transactionStatisticsMap.containsKey("IBM"));
    }

    @Test
    public void verifiesTransactionStatisticsSumValueAfterAddingTransaction(){

        InstrumentTransaction instrumentTransaction = new InstrumentTransaction("IBM",200.00,98798798798L);
        TransactionStatisticsAggregator transactionStatisticsAggregator = new TransactionStatisticsAggregator();
        transactionStatisticsAggregator.createTransactionStatistics(instrumentTransaction);
        Map<String,TransactionStatistics> transactionStatisticsMap = transactionStatisticsAggregator.getTransactionStatisticsMap();
        assertNotNull(transactionStatisticsMap);
        TransactionStatistics transactionStatistics = transactionStatisticsMap.get("IBM");
        assertNotNull(transactionStatistics);
        assertEquals(200.00,transactionStatistics.getSum(),0);

    }

    @Test
    public void verifiesTransactionStatisticsAvgValueAfterAddingTransaction(){

        InstrumentTransaction instrumentTransaction = new InstrumentTransaction("IBM",200.00,98798798798L);
        TransactionStatisticsAggregator transactionStatisticsAggregator = new TransactionStatisticsAggregator();
        transactionStatisticsAggregator.createTransactionStatistics(instrumentTransaction);
        Map<String,TransactionStatistics> transactionStatisticsMap = transactionStatisticsAggregator.getTransactionStatisticsMap();
        assertNotNull(transactionStatisticsMap);
        TransactionStatistics transactionStatistics = transactionStatisticsMap.get("IBM");
        assertNotNull(transactionStatistics);
        assertEquals(200.00,transactionStatistics.getAvg(),0);

    }

    @Test
    public void verifiesTransactionStatisticsCountValueAfterAddingTransaction(){

        InstrumentTransaction instrumentTransaction = new InstrumentTransaction("IBM",200.00,98798798798L);
        TransactionStatisticsAggregator transactionStatisticsAggregator = new TransactionStatisticsAggregator();
        transactionStatisticsAggregator.createTransactionStatistics(instrumentTransaction);
        Map<String,TransactionStatistics> transactionStatisticsMap = transactionStatisticsAggregator.getTransactionStatisticsMap();
        assertNotNull(transactionStatisticsMap);
        TransactionStatistics transactionStatistics = transactionStatisticsMap.get("IBM");
        assertNotNull(transactionStatistics);
        assertEquals(1,transactionStatistics.getCount(),0);

    }

    @Test
    public void verifiesTransactionStatisticsMinValueAfterAddingTransaction(){

        InstrumentTransaction instrumentTransaction = new InstrumentTransaction("IBM",200.00,98798798798L);
        TransactionStatisticsAggregator transactionStatisticsAggregator = new TransactionStatisticsAggregator();
        transactionStatisticsAggregator.createTransactionStatistics(instrumentTransaction);
        Map<String,TransactionStatistics> transactionStatisticsMap = transactionStatisticsAggregator.getTransactionStatisticsMap();
        assertNotNull(transactionStatisticsMap);
        TransactionStatistics transactionStatistics = transactionStatisticsMap.get("IBM");
        assertNotNull(transactionStatistics);
        assertEquals(200.00, transactionStatistics.getMin(),0);

    }

    @Test
    public void verifiesAvgInMergedResultsAfterAddingTransaction(){

        InstrumentTransaction instrumentTransaction = new InstrumentTransaction("IBM",500.00,98798798798L);
        TransactionStatisticsAggregator transactionStatisticsAggregator = new TransactionStatisticsAggregator();
        transactionStatisticsAggregator.createTransactionStatistics(instrumentTransaction);

        TransactionStatistics transactionStatistics = new TransactionStatistics(200.00,600.00,200.00,200.00,1L,98379837482374L);
        transactionStatisticsAggregator.mergeToResult(transactionStatistics,1);
        assertEquals(550.00,transactionStatistics.getAvg(),0);

    }

    @Test
    public void verifiesSumInMergedResultsAfterAddingTransaction(){

        InstrumentTransaction instrumentTransaction = new InstrumentTransaction("IBM",500.00,98798798798L);
        TransactionStatisticsAggregator transactionStatisticsAggregator = new TransactionStatisticsAggregator();
        transactionStatisticsAggregator.createTransactionStatistics(instrumentTransaction);

        TransactionStatistics transactionStatistics = new TransactionStatistics(200.00,600.00,200.00,200.00,1L,98379837482374L);
        transactionStatisticsAggregator.mergeToResult(transactionStatistics,1);
        assertEquals(1100.00,transactionStatistics.getSum(),0);

    }

    @Test
    public void verifiesMinInMergedResultsAfterAddingTransaction(){

        InstrumentTransaction instrumentTransaction = new InstrumentTransaction("IBM",500.00,98798798798L);
        TransactionStatisticsAggregator transactionStatisticsAggregator = new TransactionStatisticsAggregator();
        transactionStatisticsAggregator.createTransactionStatistics(instrumentTransaction);

        TransactionStatistics transactionStatistics = new TransactionStatistics(200.00,600.00,200.00,200.00,1L,98379837482374L);
        transactionStatisticsAggregator.mergeToResult(transactionStatistics,1);
        assertEquals(500.00,transactionStatistics.getMin(),0);

    }

    @Test
    public void verifiesMaxInMergedResultsAfterAddingTransaction(){

        InstrumentTransaction instrumentTransaction = new InstrumentTransaction("IBM",500.00,98798798798L);
        TransactionStatisticsAggregator transactionStatisticsAggregator = new TransactionStatisticsAggregator();
        transactionStatisticsAggregator.createTransactionStatistics(instrumentTransaction);

        TransactionStatistics transactionStatistics = new TransactionStatistics(200.00,600.00,200.00,200.00,1L,98379837482374L);
        transactionStatisticsAggregator.mergeToResult(transactionStatistics,1);
        assertEquals(500.00,transactionStatistics.getMax(),0);

    }

}
