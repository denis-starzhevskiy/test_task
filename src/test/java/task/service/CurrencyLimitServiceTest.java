package task.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CurrencyLimitServiceTest {

    @Autowired
    CurrencyLimitService currencyLimitService;

    @Test
    void getMinPriceByCurrencyName() {
        ResponseEntity<Object> responseEntityFailed = currencyLimitService.getMinPriceByCurrencyName("NTP");
        assertEquals(responseEntityFailed.getStatusCode(), HttpStatus.BAD_REQUEST);

        ResponseEntity<Object> responseEntitySuccess = currencyLimitService.getMinPriceByCurrencyName("BTC");
        assertEquals(responseEntitySuccess.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getMaxPriceByCurrencyName() {
        ResponseEntity<Object> responseEntityFailed = currencyLimitService.getMinPriceByCurrencyName("NTP");
        assertEquals(responseEntityFailed.getStatusCode(), HttpStatus.BAD_REQUEST);

        ResponseEntity<Object> responseEntitySuccess = currencyLimitService.getMinPriceByCurrencyName("BTC");
        assertEquals(responseEntitySuccess.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getPageElements() {
        ResponseEntity<Object> responseEntityFailed = currencyLimitService.getPageElements("BTC", null, null);
        assertEquals(responseEntityFailed.getStatusCode(), HttpStatus.OK);

        ResponseEntity<Object> responseEntitySuccess = currencyLimitService.getPageElements("BTC", 1, (short) 3);
        assertEquals(responseEntitySuccess.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getCsv() throws IOException {
        ResponseEntity<Object> responseEntityFailed = currencyLimitService.getCsv();
    }
}