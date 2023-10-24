package hr.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@AutoConfigureMockRestServiceServer
class HnbCallerServiceTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HnbCallerService hnbCallerService;

    @BeforeEach
    public void setUp() {
        this.server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetUSDMiddleExchangeRate() throws JsonProcessingException {

        String mockResponse = "[{\"srednji_tecaj\":\"1.5555\"}]";

        server.expect(ExpectedCount.once(),
                        requestTo("https://api.hnb.hr/tecajn-eur/v3?valuta=USD"))
                .andExpect(method(GET))
                .andRespond(withSuccess(mockResponse, MediaType.TEXT_PLAIN));

        BigDecimal result = hnbCallerService.getUSDMiddleExchangeRate();
        assertEquals(new BigDecimal("1.5555"), result);
    }

}
