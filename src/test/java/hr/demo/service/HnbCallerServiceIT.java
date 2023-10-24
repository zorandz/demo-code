package hr.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HnbCallerServiceIT {

    @Autowired
    private HnbCallerService hnbCallerService;

    @Test
    void HnbCallerService_testGetUSDMiddleExchangeRate_returnsExchangeRateGreaterThanZero() throws JsonProcessingException {
        BigDecimal usdMiddleExchangeRate = hnbCallerService.getUSDMiddleExchangeRate();

        assertNotNull(usdMiddleExchangeRate);
        assertTrue(usdMiddleExchangeRate.compareTo(BigDecimal.ZERO) > 0);
    }

}