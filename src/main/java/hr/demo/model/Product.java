package hr.demo.model;

import java.math.BigDecimal;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hr.demo.dto.ProductDTO;
import hr.demo.utility.ProductMapper;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import jakarta.validation.constraints.NotEmpty;

/**
 * The Product entity represents a product in the database.
 *
 * @author Zoran Džoić
 */
@Entity
@Table(name = "product")
public class Product implements ProductMapper {
	
	@Id
	@SequenceGenerator(name = "ingemark-seq2", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingemark-seq2")
	private Long productId; 
	
	@Column(name = "code", length = 10)
	private String code; 
	
	@Column(name = "name", length = 15, nullable = false)
	private String name;
	
	@Column(name = "price_eur")
	private BigDecimal priceEur;
	
	@Column(name = "price_usd")
	private BigDecimal priceUsd;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "is_available")
	private Boolean isAvailable;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "category_id")
	private ProductCategory category;
	
	@Transient
	private String stringCategoryId;

	public Product() {};

	public Product(Long productId, String code, String name, BigDecimal priceEur, BigDecimal priceUsd, String description, Boolean isAvailable, ProductCategory category, String stringCategoryId) {
		this.productId = productId;
		this.code = code;
		this.name = name;
		this.priceEur = priceEur;
		this.priceUsd = priceUsd;
		this.description = description;
		this.isAvailable = isAvailable;
		this.category = category;
		this.stringCategoryId = stringCategoryId;
	}

	public Product(String name, BigDecimal priceEur, String description, Boolean isAvailable, String categoryId) {
		this.name = name;
		this.priceEur = priceEur;
		this.description = description;
		this.isAvailable = isAvailable;
		this.stringCategoryId = categoryId;
	}


	public String getStringCategoryId() {
		return stringCategoryId;
	}

	public void setStringCategoryId(String stringCategoryId) {
		this.stringCategoryId = stringCategoryId;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPriceEur() {
		return priceEur;
	}

	public void setPriceEur(BigDecimal priceEur) {
		this.priceEur = priceEur;
	}

	public BigDecimal getPriceUsd() {
		return priceUsd;
	}

	public void setPriceUsd(BigDecimal priceUsd) {
		this.priceUsd = priceUsd;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

}
