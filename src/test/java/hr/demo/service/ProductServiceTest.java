package hr.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hr.demo.dto.ProductDTO;
import hr.demo.exceptions.ProductNotFoundException;
import hr.demo.junitextension.TimingExtension;
import hr.demo.model.Product;
import hr.demo.model.ProductCategory;
import hr.demo.repository.ProductRepo;
import hr.demo.serviceimpl.ProductServiceImpl;
import hr.demo.utility.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, TimingExtension.class})
class ProductServiceTest {

    private Product product;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private HnbCallerService hnbCallerService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        this.product = new Product();
        ProductCategory prodCateg = new ProductCategory();
        prodCateg.setId(1L);
        this.product.setProductId(1L);
        this.product.setName("product_1");
        this.product.setCode("0123456789");
        this.product.setPriceEur(BigDecimal.valueOf(10));
        this.product.setIsAvailable(true);
        this.product.setCategory(prodCateg);

    }

    @Test
    public void ProductService_createProduct_returnProductDTO() throws JsonProcessingException {

        when(productRepo.save(Mockito.any(Product.class))).thenReturn(this.product);
        when(this.hnbCallerService.getUSDMiddleExchangeRate()).thenReturn(BigDecimal.valueOf(11));

        ProductDTO savedProduct = productService.createProduct(product.getName(), product.getPriceEur(), product.getDescription(), product.getIsAvailable(), product.getCategory().getId().toString());

        assertThat(savedProduct).isNotNull();
    }

    @Test
    public void ProductService_findAllProducts_returnPageOfProductDTOs() {
        List<Product> products = Arrays.asList(new Product("Product 1", new BigDecimal(100.00), "This is the first product.", true, "1"));
        Mockito.when(productRepo.findAll(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(products));

        ProductDTO productDTO = new ProductDTO("Product 1", new BigDecimal(100.00), "This is the first product.", true, "1");
        Mockito.when(productMapper.map(Mockito.any(Product.class))).thenReturn(productDTO);

        Page<ProductDTO> productDTOPage = productService.findAll(PageRequest.of(0, 1));

        assertThat(productDTOPage).isNotNull();
    }

    @Test
    public void ProductService_updateProduct_returnsProductDTO() throws JsonProcessingException {
        when(this.productRepo.findByCode(Mockito.any(String.class))).thenReturn(this.product);
        when(this.productRepo.save(Mockito.any(Product.class))).thenReturn(this.product);

        ProductDTO savedProduct = this.productService.updateProduct(this.product.getCode(), this.product.getName(), this.product.getPriceEur(), this.product.getDescription(), this.product.getIsAvailable());

        assertThat(savedProduct).isNotNull();
    }

    @Test
    public void ProductService_getProductByCode_returnProductDTO() {
        when(this.productRepo.findByCode(Mockito.any(String.class))).thenReturn(this.product);

        ProductDTO foundProduct = this.productService.getProductByCode(this.product.getCode());

        assertThat(foundProduct).isNotNull();
    }

    @Test
    public void ProductService_deleteProductByCode_Success() {
        when(productRepo.findByCode("test_delete")).thenReturn(product);

        assertAll(() -> productService.deleteProductByCode("test_delete"));

        verify(productRepo, times(1)).findByCode("test_delete");
        verify(productRepo, times(1)).deleteById(1L);
    }

    @Test
    public void ProductService_deleteProductByCode_ProductNotFound() {
        when(productRepo.findByCode("test_delete")).thenReturn(null);

        org.junit.jupiter.api.Assertions.assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProductByCode("test_delete");
        });

        verify(productRepo, times(1)).findByCode("test_delete");
    }

    @Test
    public void ProductService_checkWhetherCodeExists_returnsProductCodeIfProductCodeDoesNotExist() {
        when(productRepo.existsByCode("some-code")).thenReturn(false);

        String productCode = productService.checkWhetherCodeExists("some-code");

        assertThat(productCode).isEqualTo("some-code");
    }

    @Test
    public void ProductService_checkWhetherCodeExists_returnsUniqueProductCode() {
        when(productRepo.existsByCode("existing-code")).thenReturn(true);

        String productCode = productService.checkWhetherCodeExists("existing-code");

        assertThat(productCode).isNotEqualTo("existing-code");
    }

    @Test
    public void ProductService_checkWhetherCodeExists_doesNotThrowAnyExceptions() {
        String productCode = productService.checkWhetherCodeExists("random-product-code");

        assertThatNoException().isThrownBy(() -> productService.checkWhetherCodeExists("random-product-code"));
    }

    @Test
    public void ProductService_generateProductCode_returnsStringOf10Characters() {
        String productCode = productService.generateProductCode();

        assertThat(productCode.length()).isEqualTo(10);
    }

    @Test
    public void ProductService_calculatePriceInUSD_returnCorrectPriceInUSD() throws JsonProcessingException {
        when(hnbCallerService.getUSDMiddleExchangeRate()).thenReturn(BigDecimal.valueOf(1.1555));

        BigDecimal priceInUSD = productService.calculatePriceInUSD(BigDecimal.valueOf(100));

        assertThat(priceInUSD).isEqualTo(BigDecimal.valueOf(115.55));
    }

    @Test
    public void ProductService_calculatePriceInUSD_throwsJsonProcessingException() throws JsonProcessingException {
        when(hnbCallerService.getUSDMiddleExchangeRate()).thenThrow(JsonProcessingException.class);

        assertThrows(JsonProcessingException.class, () -> {
            productService.calculatePriceInUSD(new BigDecimal("10"));
        });
    }
}