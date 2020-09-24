package com.realtime.statistics.service;

import com.realtime.statistics.RealtimeStatisticsApplication;
import com.realtime.statistics.exception.TransactionOutOfRangeException;
import com.realtime.statistics.model.InstrumentTransaction;
import com.realtime.statistics.model.TransactionStatistics;
import com.realtime.statistics.service.impl.TransactionStatisticsCacheImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RealtimeStatisticsApplication.class})
public class TransactionStatisticsCacheImplTest {

    @Autowired
    TransactionStatisticsCacheImpl transactionStatisticsCache;

    @Before
    public void clearCache(){
        transactionStatisticsCache.initialiseTransactionStatisticsAggregator();
    }

    @Test
    public void verifiesMaxIfTransactionsIsAddedSuccessfully() {

        InstrumentTransaction instrumentTransactionIbm = new InstrumentTransaction("IBM",400.00, Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionCts = new InstrumentTransaction("CTS",500.00,Instant.now().toEpochMilli());
        transactionStatisticsCache.addTransaction(instrumentTransactionIbm);
        transactionStatisticsCache.addTransaction(instrumentTransactionCts);
        TransactionStatistics transactionStatistics = transactionStatisticsCache.getTransactionStatistics();
        assertNotNull(transactionStatistics);
        assertEquals(500.00,transactionStatistics.getMax(),0);
    }

    @Test
    public void verifiesAvgIfTransactionsIsAddedSuccessfully() {

        InstrumentTransaction instrumentTransactionIbm = new InstrumentTransaction("IBM",400.00, Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionCts = new InstrumentTransaction("CTS",500.00,Instant.now().toEpochMilli());
        transactionStatisticsCache.addTransaction(instrumentTransactionIbm);
        transactionStatisticsCache.addTransaction(instrumentTransactionCts);
        TransactionStatistics transactionStatistics = transactionStatisticsCache.getTransactionStatistics();
        assertNotNull(transactionStatistics);
        assertEquals(450.00,transactionStatistics.getAvg(),0);
    }

    @Test
    public void verifiesMinIfTransactionsIsAddedSuccessfully() {

        InstrumentTransaction instrumentTransactionIbm = new InstrumentTransaction("IBM",400.00, Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionCts = new InstrumentTransaction("CTS",500.00,Instant.now().toEpochMilli());
        transactionStatisticsCache.addTransaction(instrumentTransactionIbm);
        transactionStatisticsCache.addTransaction(instrumentTransactionCts);
        TransactionStatistics transactionStatistics = transactionStatisticsCache.getTransactionStatistics();
        assertNotNull(transactionStatistics);
        assertEquals(400.00,transactionStatistics.getMin(),0);
    }

    @Test
    public void verifiesCountIfTransactionsIsAddedSuccessfully() {

        InstrumentTransaction instrumentTransactionIbm = new InstrumentTransaction("IBM",400.00, Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionCts = new InstrumentTransaction("CTS",500.00,Instant.now().toEpochMilli());
        transactionStatisticsCache.addTransaction(instrumentTransactionIbm);
        transactionStatisticsCache.addTransaction(instrumentTransactionCts);
        TransactionStatistics transactionStatistics = transactionStatisticsCache.getTransactionStatistics();
        assertNotNull(transactionStatistics);
        assertEquals(2,transactionStatistics.getCount(),0);
    }

    @Test
    public void verifiesCountIfTransactionsOlderThan60Secs() throws InterruptedException {

        InstrumentTransaction instrumentTransactionIbm = new InstrumentTransaction("IBM",400.00, Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionCts = new InstrumentTransaction("CTS",500.00,Instant.now().toEpochMilli());
        transactionStatisticsCache.addTransaction(instrumentTransactionIbm);
        transactionStatisticsCache.addTransaction(instrumentTransactionCts);
        TimeUnit.MINUTES.sleep(1);
        TransactionStatistics transactionStatistics = transactionStatisticsCache.getTransactionStatistics();
        assertNotNull(transactionStatistics);
        assertEquals(0,transactionStatistics.getCount(),0);
    }

    @Test(expected = TransactionOutOfRangeException.class)
    public void throwsTransactionOutOfRangeExceptionForOlderRecords() {

        InstrumentTransaction instrumentTransactionIbm = new InstrumentTransaction("IBM",400.00, Instant.now().toEpochMilli() - 60000);
        transactionStatisticsCache.addTransaction(instrumentTransactionIbm);
    }

    @Test
    public void verifiesMinIfRequestedByInstrumentName() {

        InstrumentTransaction instrumentTransactionIbmRecOne = new InstrumentTransaction("IBM",400.00, Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionCts = new InstrumentTransaction("CTS",500.00,Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionIbmRecTwo = new InstrumentTransaction("IBM",200.00, Instant.now().toEpochMilli());
        transactionStatisticsCache.addTransaction(instrumentTransactionIbmRecOne);
        transactionStatisticsCache.addTransaction(instrumentTransactionIbmRecTwo);
        transactionStatisticsCache.addTransaction(instrumentTransactionCts);
        TransactionStatistics transactionStatistics = transactionStatisticsCache.getTransactionStatistics("IBM");
        assertNotNull(transactionStatistics);
        assertEquals(200.00,transactionStatistics.getMin(),0);
    }

    @Test
    public void verifiesMaxIfRequestedByInstrumentName() {

        InstrumentTransaction instrumentTransactionIbmRecOne = new InstrumentTransaction("IBM",100.00, Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionCts = new InstrumentTransaction("CTS",300.00,Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionIbmRecTwo = new InstrumentTransaction("IBM",200.00, Instant.now().toEpochMilli());
        transactionStatisticsCache.addTransaction(instrumentTransactionIbmRecOne);
        transactionStatisticsCache.addTransaction(instrumentTransactionIbmRecTwo);
        transactionStatisticsCache.addTransaction(instrumentTransactionCts);
        TransactionStatistics transactionStatistics = transactionStatisticsCache.getTransactionStatistics("CTS");
        assertNotNull(transactionStatistics);
        assertEquals(300.00,transactionStatistics.getMax(),0);
    }

    @Test
    public void verifiesMinIfRequestedByInstrumentNameWhenSingleRecord() {

        InstrumentTransaction instrumentTransactionIbmRecOne = new InstrumentTransaction("IBM",100.00, Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionCts = new InstrumentTransaction("CTS",300.00,Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionIbmRecTwo = new InstrumentTransaction("IBM",200.00, Instant.now().toEpochMilli());
        transactionStatisticsCache.addTransaction(instrumentTransactionIbmRecOne);
        transactionStatisticsCache.addTransaction(instrumentTransactionIbmRecTwo);
        transactionStatisticsCache.addTransaction(instrumentTransactionCts);
        TransactionStatistics transactionStatistics = transactionStatisticsCache.getTransactionStatistics("CTS");

        assertNotNull(transactionStatistics);
        assertEquals(300,transactionStatistics.getMin(),0);

    }

    @Test
    public void verifiesMinIfRequestedByInstrumentNameWhenMultipleRecord() {

        InstrumentTransaction instrumentTransactionIbmRecOne = new InstrumentTransaction("IBM",100.00, Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionCts = new InstrumentTransaction("CTS",300.00,Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionIbmRecTwo = new InstrumentTransaction("IBM",200.00, Instant.now().toEpochMilli());
        transactionStatisticsCache.addTransaction(instrumentTransactionIbmRecOne);
        transactionStatisticsCache.addTransaction(instrumentTransactionIbmRecTwo);
        transactionStatisticsCache.addTransaction(instrumentTransactionCts);
        TransactionStatistics transactionStatistics = transactionStatisticsCache.getTransactionStatistics("IBM");

        assertNotNull(transactionStatistics);
        assertEquals(100,transactionStatistics.getMin(),0);

    }

    @Test
    public void verifiesAvgIfRequestedByInstrumentNameWhenMultipleRecord() {

        InstrumentTransaction instrumentTransactionIbmRecOne = new InstrumentTransaction("IBM",100.00, Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionCts = new InstrumentTransaction("CTS",300.00,Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionIbmRecTwo = new InstrumentTransaction("IBM",200.00, Instant.now().toEpochMilli());
        transactionStatisticsCache.addTransaction(instrumentTransactionIbmRecOne);
        transactionStatisticsCache.addTransaction(instrumentTransactionIbmRecTwo);
        transactionStatisticsCache.addTransaction(instrumentTransactionCts);
        TransactionStatistics transactionStatistics = transactionStatisticsCache.getTransactionStatistics("IBM");

        assertNotNull(transactionStatistics);
        assertEquals(150,transactionStatistics.getAvg(),0);

    }

    @Test
    public void verifiesMaxIfRequestedByInstrumentNameWhenMultipleRecord() {

        InstrumentTransaction instrumentTransactionIbmRecOne = new InstrumentTransaction("IBM",100.00, Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionCts = new InstrumentTransaction("CTS",300.00,Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionIbmRecTwo = new InstrumentTransaction("IBM",200.00, Instant.now().toEpochMilli());
        transactionStatisticsCache.addTransaction(instrumentTransactionIbmRecOne);
        transactionStatisticsCache.addTransaction(instrumentTransactionIbmRecTwo);
        transactionStatisticsCache.addTransaction(instrumentTransactionCts);
        TransactionStatistics transactionStatistics = transactionStatisticsCache.getTransactionStatistics("IBM");

        assertNotNull(transactionStatistics);
        assertEquals(200,transactionStatistics.getMax(),0);

    }

    @Test
    public void verifiesCountIfRequestedByInstrumentNameWhenMultipleRecord() {

        InstrumentTransaction instrumentTransactionIbmRecOne = new InstrumentTransaction("IBM",100.00, Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionCts = new InstrumentTransaction("CTS",300.00,Instant.now().toEpochMilli());
        InstrumentTransaction instrumentTransactionIbmRecTwo = new InstrumentTransaction("IBM",200.00, Instant.now().toEpochMilli());
        transactionStatisticsCache.addTransaction(instrumentTransactionIbmRecOne);
        transactionStatisticsCache.addTransaction(instrumentTransactionIbmRecTwo);
        transactionStatisticsCache.addTransaction(instrumentTransactionCts);
        TransactionStatistics transactionStatistics = transactionStatisticsCache.getTransactionStatistics("IBM");

        assertNotNull(transactionStatistics);
        assertEquals(2,transactionStatistics.getCount(),0);

    }

}
