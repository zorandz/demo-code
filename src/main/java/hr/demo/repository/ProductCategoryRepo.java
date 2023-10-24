package hr.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.demo.model.ProductCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepo extends JpaRepository<ProductCategory, Long>{

}
