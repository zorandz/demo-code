package hr.demo.config;

import hr.demo.dto.ProductDTO;
import hr.demo.utility.ProductMapper;
import hr.demo.utility.ProductMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import org.springframework.context.annotation.Configuration;

/**
 * Small configuration class that was needed to inject RestTemplate bean to Spring context
 */
@Configuration
public class Config {
	
	public Config() {}

	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

	@Bean
	public ProductMapper productMapper() {
		return new ProductMapperImpl();
	}

	@Bean
	public ProductDTO productDTO() {
		return new ProductDTO();
	}


}
