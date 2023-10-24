package hr.demo.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * This is a helper class that fires an API call to an external resource, namely HNB API
 * that we need to calculate price in USD for every newly added product.
 * 
 * @author Zoran Džoić
 */
@Service
@JsonIgnoreProperties(ignoreUnknown = true)
public class HnbCallerService {
	
	    private final RestTemplate restTemplate;

	    public HnbCallerService(RestTemplate restTemplate) {
	        this.restTemplate = restTemplate;
	    }

	    /**
	     * Makes a call to HNB API, takes middle exchange rate for USD out of the response and returns it.
	     * 
	     * @return middle exchange rate for USD
	     * @throws JsonProcessingException if there is an error processing JSON
	     */
	    public BigDecimal getUSDMiddleExchangeRate() throws JsonProcessingException {
	    	 String url = "https://api.hnb.hr/tecajn-eur/v3?valuta=USD" ;
		        String response = restTemplate.getForObject(url, String.class);

		        ObjectMapper objectMapper = new ObjectMapper();

		        TypeReference<List<LinkedHashMap<String, Object>>> typeReference = new TypeReference<List<LinkedHashMap<String, Object>>>() {};

		        List<LinkedHashMap<String, Object>> exchangeRates = objectMapper.readValue(response, typeReference);

		        String middleExchangeRateString = (String) exchangeRates.get(0).get("srednji_tecaj");
		        
		        String noCommaMiddleExchangeRateString = middleExchangeRateString.replace(",", ".");

		        return new BigDecimal(noCommaMiddleExchangeRateString);
	    }

}
