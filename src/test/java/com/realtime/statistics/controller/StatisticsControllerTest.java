package com.realtime.statistics.controller;

import com.realtime.statistics.RealtimeStatisticsApplication;
import com.realtime.statistics.model.InstrumentTransaction;
import com.realtime.statistics.model.TransactionStatistics;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import static com.realtime.statistics.utils.TestUtils.createURLWithPort;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {RealtimeStatisticsApplication.class})
@TestPropertySource(locations = "classpath:application.yaml")
public class StatisticsControllerTest {

    private HttpHeaders httpHeaders;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${server.port}")
    private String port;

    @Value("${application.test.host}")
    private String host;



    @Test
    public void getsZeroInstrumentCountWhenNoInstrumentsPosted(){

        HttpEntity<InstrumentTransaction> entity = new HttpEntity<>(getHttpHeaders());
        ResponseEntity<TransactionStatistics> response = restTemplate.exchange(createURLWithPort("/statistics",
                host, port), HttpMethod.GET, entity, TransactionStatistics.class);

        assertNotNull(response);
        assertSame(HttpStatus.OK,response.getStatusCode());
        assertEquals(0.0,response.getBody().getSum(),0);

    }

    @Test
    public void getsZeroInstrumentMaxWhenNoInstrumentsPosted(){

        HttpEntity<InstrumentTransaction> entity = new HttpEntity<>(getHttpHeaders());
        ResponseEntity<TransactionStatistics> response = restTemplate.exchange(createURLWithPort("/statistics",
                host, port), HttpMethod.GET, entity, TransactionStatistics.class);

        assertNotNull(response);
        assertSame(HttpStatus.OK,response.getStatusCode());
        assertEquals(0.0,response.getBody().getMax(),0);

    }

    @Test
    public void getsZeroInstrumentMinWhenNoInstrumentsPosted(){

        HttpEntity<InstrumentTransaction> entity = new HttpEntity<>(getHttpHeaders());
        ResponseEntity<TransactionStatistics> response = restTemplate.exchange(createURLWithPort("/statistics",
                host, port), HttpMethod.GET, entity, TransactionStatistics.class);

        assertNotNull(response);
        assertSame(HttpStatus.OK,response.getStatusCode());
        assertEquals(0.0,response.getBody().getMin(),0);

    }

    @Test
    public void getsZeroInstrumentAvgWhenNoInstrumentsPosted(){

        HttpEntity<InstrumentTransaction> entity = new HttpEntity<>(getHttpHeaders());
        ResponseEntity<TransactionStatistics> response = restTemplate.exchange(createURLWithPort("/statistics",
                host, port), HttpMethod.GET, entity, TransactionStatistics.class);

        assertNotNull(response);
        assertSame(HttpStatus.OK,response.getStatusCode());
        assertEquals(0.0,response.getBody().getAvg(),0);

    }

    @Test
    public void throwsBadRequestWhenInstrumentAddedWithoutInstrumentName(){

        InstrumentTransaction instrumentTransaction = new InstrumentTransaction(null,20.00,Instant.now().toEpochMilli());
        HttpEntity<InstrumentTransaction> entity = new HttpEntity<>(instrumentTransaction, getHttpHeaders());
        ResponseEntity<InstrumentTransaction> response = restTemplate.exchange(createURLWithPort("/tick",
                host, port), HttpMethod.POST, entity, InstrumentTransaction.class);

        assertNotNull(response);
        assertSame(HttpStatus.BAD_REQUEST,response.getStatusCode());

    }

    @Test
    public void returnsHttpStatusCreatedWhenInstrumentAddedSuccessfully(){

        InstrumentTransaction instrumentTransaction = new InstrumentTransaction("IBM",20.00, Instant.now().toEpochMilli());
        HttpEntity<InstrumentTransaction> entity = new HttpEntity<>(instrumentTransaction, getHttpHeaders());
        ResponseEntity<InstrumentTransaction> response = restTemplate.exchange(createURLWithPort("/tick",
                host, port), HttpMethod.POST, entity, InstrumentTransaction.class);

        assertNotNull(response);
        assertSame(HttpStatus.CREATED,response.getStatusCode());

    }



    @Test
    public void returnsHttpStatusNoContentWhenInstrumentAddedWithOldTimestamp(){

        InstrumentTransaction instrumentTransaction = new InstrumentTransaction("IBM",20.00, 1600621284797L);
        HttpEntity<InstrumentTransaction> entity = new HttpEntity<>(instrumentTransaction, getHttpHeaders());
        ResponseEntity<InstrumentTransaction> response = restTemplate.exchange(createURLWithPort("/tick",
                host, port), HttpMethod.POST, entity, InstrumentTransaction.class);

        assertNotNull(response);
        assertSame(HttpStatus.NO_CONTENT,response.getStatusCode());

    }

    @Test
    public void returnsValidStatisticsWhenInstrumentAddedSuccessfully(){

        InstrumentTransaction instrumentTransaction = new InstrumentTransaction("IBM",100.00, Instant.now().toEpochMilli());
        HttpEntity<InstrumentTransaction> entity = new HttpEntity<>(instrumentTransaction, getHttpHeaders());
        restTemplate.exchange(createURLWithPort("/tick",
                host, port), HttpMethod.POST, entity, InstrumentTransaction.class);

        ResponseEntity<TransactionStatistics> resp = restTemplate.exchange(createURLWithPort("/statistics",
                host, port), HttpMethod.GET, entity, TransactionStatistics.class);

        assertNotNull(resp);
        assertSame(1L,resp.getBody().getCount());
        assertEquals(100.00,resp.getBody().getAvg(),0);
        assertEquals(100.00,resp.getBody().getMax(),0);
        assertEquals(100.00,resp.getBody().getMin(),0);

    }

    @Test
    public void returnsInstrumentSpecificStatisticsWhenInstrumentAddedSuccessfully(){

        InstrumentTransaction instrumentTransactionIbm100 = new InstrumentTransaction("IBM",100.00, Instant.now().toEpochMilli());
        HttpEntity<InstrumentTransaction> entityIbm100 = new HttpEntity<>(instrumentTransactionIbm100, getHttpHeaders());
        restTemplate.exchange(createURLWithPort("/tick",
                host, port), HttpMethod.POST, entityIbm100, InstrumentTransaction.class);

        InstrumentTransaction instrumentTransactionIbm200 = new InstrumentTransaction("IBM",200.00, Instant.now().toEpochMilli());
        HttpEntity<InstrumentTransaction> entityIbm200 = new HttpEntity<>(instrumentTransactionIbm200, getHttpHeaders());
        restTemplate.exchange(createURLWithPort("/tick",
                host, port), HttpMethod.POST, entityIbm200, InstrumentTransaction.class);

        HttpEntity<InstrumentTransaction> entity = new HttpEntity<>(getHttpHeaders());
        ResponseEntity<TransactionStatistics> resp = restTemplate.exchange(createURLWithPort("/statistics/IBM",
                host, port), HttpMethod.GET, entity, TransactionStatistics.class);

        assertNotNull(resp);
        assertSame(2L,resp.getBody().getCount());
        assertEquals(150.00,resp.getBody().getAvg(),0);
        assertEquals(200.00,resp.getBody().getMax(),0);
        assertEquals(100.00,resp.getBody().getMin(),0);

    }


    private HttpHeaders getHttpHeaders(){

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(mediaTypeList);
        return httpHeaders;
    }


}
