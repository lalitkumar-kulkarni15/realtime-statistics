package com.realtime.statistics.controller;

import com.realtime.statistics.model.InstrumentTransaction;
import com.realtime.statistics.model.TransactionStatistics;
import com.realtime.statistics.service.TransactionCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import javax.validation.Valid;

@RestController
@Api(tags = "Realtime statistics API", value = "This API has 3 endpoints which persists the instrument transactions and returns the aggregated statistics of the last 60 secs.")
public class StatisticsController {

    private TransactionCache transactionCache;

    public StatisticsController(TransactionCache transactionCache) {
        this.transactionCache = transactionCache;
    }


    @ApiOperation(value = "Add a new instrument transaction", notes = "This API persists a new instrument transaction")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Instrument transaction is added successfully"),
            @ApiResponse(code = 204, message = "The instrument transaction is older than 60 sec hence it is ignored")})
    @ApiResponse(code = 400, message = "Bad input request.Please check the error description for more details.")
    @PostMapping("/tick")
    public ResponseEntity<Object> addInstrumentTransaction(@Valid @RequestBody InstrumentTransaction instrumentTransaction) {
        transactionCache.addTransaction(instrumentTransaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get aggregated statistics", notes = "Gets the aggregated statistics of instruments of last 60 seconds")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Aggregated statistics fetched successfully")})
    @GetMapping("/statistics")
    public ResponseEntity<TransactionStatistics> getStatistics() {
        final TransactionStatistics transactionStatistics = transactionCache.getTransactionStatistics();
        return new ResponseEntity<>(transactionStatistics, HttpStatus.OK);
    }

    @ApiOperation(value = "Get aggregated statistics by instrument", notes = "Gets the aggregated statistics of instruments of last 60 seconds by instrument")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Aggregated statistics fetched successfully")})
    @GetMapping("/statistics/{instrument_identifier}")
    public ResponseEntity<TransactionStatistics> getStatistics(@PathVariable(name = "instrument_identifier") String instrumentIdentifier) {
        final TransactionStatistics transactionStatistics = transactionCache.getTransactionStatistics(instrumentIdentifier);
        return new ResponseEntity<>(transactionStatistics, HttpStatus.OK);
    }

}
