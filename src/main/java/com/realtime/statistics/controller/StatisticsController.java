package com.realtime.statistics.controller;

import com.realtime.statistics.model.InstrumentTransaction;
import com.realtime.statistics.model.TransactionStatistics;
import com.realtime.statistics.service.StatisticsCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@Api(tags = "Statistics API", value = "This API houses endpoints to accept the instrument transactions and returns the aggregated statistics of the same."
		+ "for the past configurable duration.")
public class StatisticsController {

    private StatisticsCacheService statisticsCacheService;

    public StatisticsController(StatisticsCacheService statisticsCacheService) {
        this.statisticsCacheService = statisticsCacheService;
    }

	/*
	 * @ApiOperation(value = "Add a new instrument transaction", notes =
	 * "This endpoint adds a new instrument transaction to the data store.")
	 * 
	 * @ApiResponses(value = {
	 * 
	 * @ApiResponse(code = 201, message =
	 * "Instrument transaction has been successfully added in the data store"),
	 * 
	 * @ApiResponse(code = 204, message =
	 * "No content. The input instrument transaction is older and cannot be accepted"
	 * )})
	 * 
	 * @ApiResponse(code = 400, message = "Invalid request")
	 */
    @ApiOperation(value = "Create a new Job Application.", notes = "This API creates a new Job" +
            " Application and stores in the data store.")
@ApiResponses(value = {@ApiResponse(code = 500, message = "Something went wrong in the service, please "
             + "contact the system administrator - Email - lalitkulkarniofficial@gmail.com"),
@ApiResponse(code = 201, message = "Job Application has been successfully created in the system.")})
@ApiResponse(code = 400, message = "Bad input request.Please check the error description for more details.")
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

    @GetMapping("/statistics/{instrument_identifier}")
    public ResponseEntity<TransactionStatistics> getStatistics(@PathVariable(name = "instrument_identifier") String instrumentIdentifier){
        TransactionStatistics transactionStatistics = statisticsCacheService.getTransactionStatistics(instrumentIdentifier);
        return new ResponseEntity<>(transactionStatistics,HttpStatus.OK);
    }


}
