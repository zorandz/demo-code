package hr.demo.repository;

import java.util.List;

import hr.demo.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hr.demo.model.Product;

/**
 * The `ProductRepo` interface provides methods for accessing products from the database.
 *
 * @author Zoran Džoić
 */
@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
	
	Page<Product> findAll(Pageable pageable);
	
	public List<Product> findAll();
	
	/**
	 * Finds a product by its ID.
	 *
	 * @param id the product ID
	 * @return the product, or null if not found
	 */
	Product findByProductId(Long id);

	/**
	 * Finds a product by its code.
	 *
	 * @param code the product code
	 * @return the product, or null if not found
	 */
	Product findByCode(String code);

	/**
	 * Checks if a product with the given code exists.
	 *
	 * @param productCode the product code
	 * @return true if the product exists, false otherwise
	 */
	boolean existsByCode(String productCode);

	void deleteById(Long id);

}
