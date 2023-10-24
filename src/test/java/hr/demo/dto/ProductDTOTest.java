package hr.demo.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductDTOTest {

    @Autowired
    private ProductDTO productDTO;

    @Autowired
    private Validator validator;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO(
                "1234567890",
                "product_1",
                BigDecimal.valueOf(250),
                BigDecimal.valueOf(270),
                true,
                "opis",
                "1");
    }

    @Test
    public void testEmptyName() {
        productDTO.setName(null);
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testMaximumNameLength() {
        productDTO.setName("jedan dva tri četiri pet šest sedam osam devet deset šesnaest");
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testDecimalMinOnPriceEur() {
        productDTO.setPriceEUR(BigDecimal.valueOf(0));
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testThatDescriptionCanNotBeEmpty() {
        productDTO.setDescription("");
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testThatCategoryIdCanNotBeEmpty() {
        productDTO.setCategoryId("");
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testGettersAndSetters() {
        // Test the `getName()` and `setName()` methods.
        productDTO.setName("Test Product");
        assertThat(productDTO.getName()).isEqualTo("Test Product");

        // Test the `getCode()` and `setCode()` methods.
        productDTO.setCode("1234567890");
        assertThat(productDTO.getCode()).isEqualTo("1234567890");

        // Test the `getPriceEUR()` and `setPriceEUR()` methods.
        productDTO.setPriceEUR(BigDecimal.valueOf(100));
        assertThat(productDTO.getPriceEUR()).isEqualTo(BigDecimal.valueOf(100));

        // Test the `getPriceUSD()` and `setPriceUSD()` methods.
        productDTO.setPriceUSD(BigDecimal.valueOf(120));
        assertThat(productDTO.getPriceUSD()).isEqualTo(BigDecimal.valueOf(120));

        // Test the `getDescription()` and `setDescription()` methods.
        productDTO.setDescription("This is a test product.");
        assertThat(productDTO.getDescription()).isEqualTo("This is a test product.");

        // Test the `getIsAvailable()` and `setIsAvailable()` methods.
        productDTO.setIsAvailable(true);
        assertThat(productDTO.getIsAvailable()).isTrue();

        // Test the `getCategoryId()` and `setCategoryId()` methods.
        productDTO.setCategoryId("1");
        assertThat(productDTO.getCategoryId()).isEqualTo("1");
    }
}