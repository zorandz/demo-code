package hr.demo.serviceimpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import hr.demo.exceptions.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import hr.demo.dto.ProductDTO;
import hr.demo.model.Product;
import hr.demo.utility.ProductMapper;
import hr.demo.model.ProductCategory;
import hr.demo.repository.ProductRepo;
import hr.demo.service.ProductService;
import hr.demo.service.HnbCallerService;

/**
 * Implementation of ProductService interface.
 * Implements methods for creating, updating, deleting, and retrieving products.
 * 
 * @author Zoran Džoić
 */
@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private ProductMapper productMapper;

	Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private HnbCallerService hnbCallerService;

	/**
	 * Creates a new product.
	 * 
	 * @param name the name of the product
	 * @param priceInEur the price of the product in euros
	 * @param description the description of the product
	 * @param isAvailable whether the product is available
	 * @return the created product
	 * 
	 * @throws JsonMappingException if there is an error mapping JSON
	 * @throws JsonProcessingException if there is an error processing JSON
	 */
	@Override
	public ProductDTO createProduct(String name, BigDecimal priceInEur, String description, Boolean isAvailable, String categoryId) throws JsonMappingException, JsonProcessingException {

		Product product = new Product();

		ProductCategory prodCat = new ProductCategory();

		product.setCode(checkWhetherCodeExists(generateProductCode()));
		product.setName(name);
		product.setPriceEur(priceInEur);
		product.setPriceUsd(calculatePriceInUSD(priceInEur));
		product.setDescription(description);
		product.setIsAvailable(isAvailable);

		prodCat.setId(Long.valueOf(categoryId));
		prodCat.addProduct(product);
		product.setCategory(prodCat);

		ProductDTO productDTO = new ProductDTO();

		try {
			  Product savedProduct = this.productRepo.save(product);
			  
			  productDTO = new ProductDTO(savedProduct.getCode(), savedProduct.getName(), savedProduct.getPriceEur(), savedProduct.getPriceUsd(), savedProduct.getIsAvailable(), savedProduct.getDescription(), String.valueOf(savedProduct.getCategory().getId()));
			  logger.debug("DEBUG: " + productDTO.toString());
			} catch (Exception e) {
			  logger.error("Error happened in an attempt to save the product by calling productRepo.save(product); " + e.getMessage());
			}
		return productDTO;
	}

	/**
	 * Finds all products, here is an example of http request:
	 *
	 * http://localhost:8089/products?size=7&page=0&sort=priceEur,desc
	 *
	 * @param pageable
	 * @return - returns a page of products
	 */
	@Override
	public Page<ProductDTO> findAll(Pageable pageable) {
		Page<Product> products = productRepo.findAll(pageable);
		return products.map(this.productMapper::map);
	}


	/**
	 * Updates a product.
	 * This method updates the name, price, description, and availability of a product.
	 * 
	 * @param code the code of the product to be updated
	 * @param name the new name of the product
	 * @param priceInEur the new price of the product in euros
	 * @param description the new description of the product
	 * @param isAvailable whether the product is available
	 * 
	 * @return the updated product
	 * 
	 * @throws JsonMappingException if there is an error mapping JSON
	 * @throws JsonProcessingException if there is an error processing JSON
	 */
	@Override
	public ProductDTO updateProduct(String code, String name, BigDecimal priceInEur,
			String description, Boolean isAvailable) throws JsonMappingException, JsonProcessingException {

		logger.info("Updating product with code: {}", code);

		Product product = this.productRepo.findByCode(code);

		product.setName(name);
		logger.debug("Product name set to: {}", name);

		if (product.getPriceEur().compareTo(priceInEur) != 0) {
			product.setPriceEur(priceInEur);
			product.setPriceUsd(calculatePriceInUSD(priceInEur));
			logger.debug("Product price set to: {} EUR / {} USD", product.getPriceEur() + " / " + product.getPriceUsd());
		}

		product.setDescription(description);
		product.setIsAvailable(isAvailable);
		 logger.debug("Updated product details - Description: {}, Availability: {}", 
                 description, isAvailable);

		Product savedProduct = this.productRepo.save(product);
		
	    ProductDTO productDTO = new ProductDTO(
	    	      savedProduct.getCode(), 
	    	      savedProduct.getName(),
	    	      savedProduct.getPriceEur(),
	    	      savedProduct.getPriceUsd(),
	    	      savedProduct.getIsAvailable(),
	    	      savedProduct.getDescription(),
	    	      savedProduct.getCategory().getId().toString()
	    	    );
	    
		return productDTO;
	}

	/**
	 * This method deletes a product from the database.
	 *
	 *
	 * @param code - the code of the product to be deleted
	 * @return the deleted product
	 * @throws ProductNotFoundException, if the product can not be found
	 */
	@Override
	public void deleteProductByCode(String code) {
		Product product = this.productRepo.findByCode(code);

		if (product == null) {
			throw new ProductNotFoundException(code);
		} else {
			this.productRepo.deleteById(product.getProductId());
		}
	}

	/**
	 * Finds product by code.
	 * 
	 * @param code - the code of the product to be searched for
	 * @return product object or null
	 */
	@Override
	public ProductDTO getProductByCode(String code) {
		Product product = this.productRepo.findByCode(code);
		return product.map(product);
	}



	/**
	 * This is a helper method that uses product repository to search for a product by product code,
	 * it is used when generating a new code for a new product, because of the fact that product
	 * code is unique and generated randomly by us
	 * 
	 * @param productCode - newly generated product code
	 * @return products's final code
	 */
	public String checkWhetherCodeExists(String productCode) {
		while (productRepo.existsByCode(productCode)) {
			productCode = generateProductCode();
		}

		return productCode;
	};

	/**
	 * Helper method that contains logic of generating a random code for a product.
	 * 
	 * @return newly generated product code
	 */
	public String generateProductCode() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwz0123456789";
		Random random = new Random();
		String productCode = "";
		for (int i = 0; i < 10; i++) {
			productCode += characters.charAt(random.nextInt(characters.length()));
		}

		return productCode;
	}

	/**
	 * Calculates price in USD for every product.
	 * 
	 * Price in USD we don't get from the user, but we calculate it by ourselves by calling the HNB API
	 * and by using middle exchange rate.
	 * 
	 * @param priceInEUR - the price in EUR that we get from the user
	 * @return returns rounded price
	 * @throws JsonProcessingException if there is an error processing JSON while fetching exchange rate
	 */
	public BigDecimal calculatePriceInUSD(BigDecimal priceInEUR) throws JsonProcessingException {

		BigDecimal dollarExchangeRate = this.hnbCallerService.getUSDMiddleExchangeRate();

		BigDecimal priceInUSD = priceInEUR.multiply(dollarExchangeRate);

		return priceInUSD.setScale(2, RoundingMode.CEILING);
	}
}
