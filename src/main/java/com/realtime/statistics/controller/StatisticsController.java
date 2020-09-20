package com.realtime.statistics.controller;

import com.realtime.statistics.model.InstrumentTransaction;
import com.realtime.statistics.model.TransactionStatistics;
import com.realtime.statistics.service.StatisticsCacheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
public class StatisticsController {

    private StatisticsCacheService statisticsCacheService;

    public StatisticsController(StatisticsCacheService statisticsCacheService) {
        this.statisticsCacheService = statisticsCacheService;
    }

    @PostMapping("/tick")
    public ResponseEntity<Object> addInstrumentTransaction(@Valid @RequestBody InstrumentTransaction instrumentTransaction) {
        statisticsCacheService.addTransaction(instrumentTransaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/statistics")
    public ResponseEntity<TransactionStatistics> getStatistics(){
        TransactionStatistics transactionStatistics = statisticsCacheService.getTransactionStatistics();
        return new ResponseEntity<>(transactionStatistics,HttpStatus.OK);
    }


}
