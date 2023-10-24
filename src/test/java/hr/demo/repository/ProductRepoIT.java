package hr.demo.repository;

import hr.demo.model.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepoIT {

    private Product product;

    @Autowired
    private ProductRepo productRepo;

    @BeforeEach
    void setUp() {
        this.product = new Product();
        this.product.setName("product_1");
        this.product.setCode("1234567890");
    }

    @Test
    void ProductRepo_findAll_returnAllProducts() {

        Product product2 = new Product();
        product2.setName("product_2");

        this.productRepo.saveAll(Arrays.asList(this.product, product2));

        List<Product> products = productRepo.findAll();

        Assertions.assertThat(products).isNotNull();
        Assertions.assertThat(products.size()).isEqualTo(2);
    }

    @Test
    void productRepo_findByProductId_returnProductNotNull() {
        this.productRepo.save(this.product);

        List<Product> products = productRepo.findAll();

        Product savedProduct = this.productRepo.findByProductId(1L);

        Assertions.assertThat(savedProduct).isNotNull();
    }

    @Test
    void productRepo_findByCode_returnProductNotNull() {
        this.productRepo.save(this.product);

        Product returnedProduct = this.productRepo.findByCode(this.product.getCode());

        Assertions.assertThat(returnedProduct).isNotNull();
    }

    @Test
    void ProductRepo_existsByCode_returnTrueIfExists() {
        this.productRepo.save(this.product);

        Boolean isInside = this.productRepo.existsByCode(this.product.getCode());

        Assertions.assertThat(isInside).isEqualTo(true);
    }
}