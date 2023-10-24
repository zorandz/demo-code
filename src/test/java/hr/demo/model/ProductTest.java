package hr.ingemark.careers.model;

import hr.demo.model.Product;
import hr.demo.model.ProductCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Tag("models")
class ProductTest {

    @Mock
    private ProductCategory productCategory;

    @DisplayName("Testing Product object")
    @Test
    public void groupAssertions() {

        ProductCategory productCategory = mock(ProductCategory.class);

        Product product = new Product(3L, "code", "name", BigDecimal.valueOf(120.50), BigDecimal.valueOf(127.70), "description", true, productCategory, "1");

        assertAll("Testing product fields",
                () -> assertEquals("name", product.getName(), "Product name should be name"),
                () -> assertEquals("code", product.getCode(), "Product code should be code"),
                () -> assertTrue(product.getPriceEur().compareTo(new BigDecimal("120.50")) == 0, "Product price in EUR should be 120.50"),
                () -> assertTrue(product.getPriceUsd().compareTo(new BigDecimal("127.70")) == 0, "Product price in USD should be 127.70"),
                () -> assertEquals("description", product.getDescription(), "Product description should be description"),
                () -> assertEquals(true, product.getIsAvailable(), "Product should be available"),
                () -> assertEquals(productCategory, product.getCategory(), "Product category should be " + productCategory),
                () -> assertEquals("1", product.getStringCategoryId(), "Product string category should be 1")
        );
    }

}