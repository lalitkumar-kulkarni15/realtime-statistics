package com.realtime.statistics.controller;

import com.realtime.statistics.model.InstrumentTransaction;
import com.realtime.statistics.model.TransactionStatistics;
import com.realtime.statistics.service.TransactionCache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * <p>This class exposes 3 rest endpoints which adds the instrument transaction to the data store
 * and retrieves the aggregated statistics of the last 60 seconds</p>
 */
@RestController
public class StatisticsController {

    private TransactionCache transactionCache;

    public StatisticsController(TransactionCache transactionCache) {
        this.transactionCache = transactionCache;
    }

    @PostMapping("/tick")
    public ResponseEntity<Object> addInstrumentTransaction(@Valid @RequestBody InstrumentTransaction instrumentTransaction) {
        transactionCache.addTransaction(instrumentTransaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/statistics")
    public ResponseEntity<TransactionStatistics> getStatistics() {
        final TransactionStatistics transactionStatistics = transactionCache.getTransactionStatistics();
        return new ResponseEntity<>(transactionStatistics, HttpStatus.OK);
    }

    @GetMapping("/statistics/{instrument_identifier}")
    public ResponseEntity<TransactionStatistics> getStatistics(@PathVariable(name = "instrument_identifier") String instrumentIdentifier) {
        final TransactionStatistics transactionStatistics = transactionCache.getTransactionStatistics(instrumentIdentifier);
        return new ResponseEntity<>(transactionStatistics, HttpStatus.OK);
    }

}
