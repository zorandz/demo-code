package hr.demo.service;

import java.math.BigDecimal;
import java.util.List;

import hr.demo.exceptions.ProductNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import hr.demo.dto.ProductDTO;
import hr.demo.model.Product;

/**
 * The ProductService interface provides methods for creating, updating, deleting, and retrieving products.
 * 
 * @author Zoran Džoić
 */
@Service
public interface ProductService {

	ProductDTO createProduct(String name, BigDecimal priceEur, String description, Boolean isAvailable, String categoryId) throws JsonMappingException, JsonProcessingException;

	ProductDTO updateProduct(String code, String name, BigDecimal priceInEur, String description, Boolean isAvailable) throws JsonMappingException, JsonProcessingException;

	void deleteProductByCode(String code) throws ProductNotFoundException;

	ProductDTO getProductByCode(String code);

	Page<ProductDTO> findAll(Pageable pageable);
}
