package hr.demo.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import hr.demo.exceptions.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;

import hr.demo.dto.ProductDTO;
import hr.demo.model.ProductCategory;
import hr.demo.repository.ProductCategoryRepo;
import hr.demo.service.ProductService;
import hr.demo.utility.HttpResponse;
import jakarta.validation.Valid;

import java.util.Date;

/**
 * A REST controller that provides APIs for managing products.
 * <p>
 * The following APIs are provided:
 * <p>
 * /products  - Returns a list of all products in the database.
 * /products/add-product: Adds a new product to the database.
 * /products/{code} Returns the product by the specified code.
 * /products/update-product Updates the product containing the specified code.
 * /products/delete/{code} Deletes the product by the specified code.
 *
 * @author Zoran Džoić
 */
@RestController
@RequestMapping("/products")
public class ProductController {
    public static final String PRODUCT_DELETED_SUCCESSFULLY = "Product successfully deleted.";
    public static final String PRODUCT_WAS_NOT_FOUND = "The product was not found.";

    Logger logger = LoggerFactory.getLogger(ProductController.class);


    private final ProductService productService;

    @Autowired(required = false)
    ProductCategoryRepo productCatRepo;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Adds a new product to the database.
     *
     * @param product The product to be added.
     * @return A response entity containing the newly created product.
     */
    @PostMapping("/add-product")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid ProductDTO productDTO) throws JsonProcessingException {

        ProductDTO savedProduct = productService.createProduct(productDTO.getName(), productDTO.getPriceEUR(), productDTO.getDescription(), productDTO.getIsAvailable(), productDTO.getCategoryId());
        logger.debug("Saved product under the code " + savedProduct.getCode());
        return new ResponseEntity<ProductDTO>(savedProduct, HttpStatus.CREATED);
    }

    /**
     * Returns a list of all products in the database.
     * url: http://localhost:8089/products?page=0&size=3
     *
     * @return A response entity containing a list of products.
     * @throws ResponseStatusException If the response status code is not 200 OK.
     */
    @GetMapping
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        return productService.findAll(pageable);
    }


    @PostMapping("/add-product-category")
    public ResponseEntity<ProductCategory> addNewCategory(@RequestBody ProductCategory productCategory) {

        ProductCategory pc = this.productCatRepo.save(productCategory);

        return new ResponseEntity<ProductCategory>(pc, HttpStatus.CREATED);
    }

    /**
     * Returns the product with the specified code.
     *
     * @param code. The code of the product to be retrieved.
     * @return A response entity containing the product.
     * @throws ResponseStatusException If the response status code is not 200 OK.
     */
    @GetMapping("/product/{code}")
    public ResponseEntity<ProductDTO> getProductDetail(@PathVariable("code") String code) {
        ProductDTO product = productService.getProductByCode(code);

        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUCT_WAS_NOT_FOUND);
        } else {
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
    }

    /**
     * Updates the product with the specified code.
     * This method updates the product's name, price, description, and availability.
     *
     * @param product The product to be updated.
     * @return A response entity containing the updated product.
     * @throws ResponseStatusException If the response status code is not 200 OK.
     */
    @PutMapping("/update-product")
    public ResponseEntity<Object> updateProduct(@RequestBody @Valid ProductDTO productDTO) throws JsonProcessingException {
            ProductDTO updatedProduct = productService.updateProduct(productDTO.getCode(), productDTO.getName(), productDTO.getPriceEUR(), productDTO.getDescription(), productDTO.getIsAvailable());

            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    /**
     * Deletes the product with the specified code.
     *
     * @param code. The code of the product to be deleted.
     * @return A response entity containing the deleted product.
     */
    @DeleteMapping("/delete-product/{code}")
    public ResponseEntity<Object> deleteProduct(@PathVariable String code) {

        try {
            this.productService.deleteProductByCode(code);
            return new ResponseEntity<>(PRODUCT_DELETED_SUCCESSFULLY, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            httpResponse.setStatus(HttpStatus.NOT_FOUND);
            httpResponse.setReason(PRODUCT_WAS_NOT_FOUND);
            httpResponse.setMessage(e.getMessage());
            httpResponse.setTimeStamp(String.valueOf(new Date(System.currentTimeMillis())));

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(httpResponse);
        }
    }

}
