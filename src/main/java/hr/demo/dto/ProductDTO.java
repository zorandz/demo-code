package hr.demo.dto;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
public class ProductDTO {

	@NotEmpty(message = "Name can not be empty")
	@Length(min = 3, max = 15, message = "The name must be at least 3 characters long and maximum 15 characters long.")
	private String name;
	
	private String code;

	@NotEmpty(message = "Description can not be empty")
	private String description;

	@DecimalMin(value = "0", inclusive = false, message = "Price must be greater than zero")
	private BigDecimal priceEUR;
	
	private BigDecimal priceUSD;
	
	private Boolean isAvailable;
	
	@NotEmpty(message = "Category ID can not be empty")
	private String categoryId;

	public ProductDTO(String code, String name, BigDecimal priceEur, BigDecimal priceUsd, Boolean isAvailable, String description, String categoryId) {
		this.code = code;
		this.name = name;
		this.priceEUR = priceEur;
		this.priceUSD = priceUsd;
		this.isAvailable = isAvailable;
		this.description = description;
		this.categoryId = categoryId;
	}

    public ProductDTO(String name, BigDecimal priceEur, String description, Boolean isAvailable, String categoryId) {
		this.name = name;
		this.priceEUR = priceEur;
		this.description = description;
		this.isAvailable = isAvailable;
		this.categoryId = categoryId;
	}


    public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public BigDecimal getPriceEUR() {
		return priceEUR;
	}
	public BigDecimal getPriceUSD() {
		return priceUSD;
	}
	public void setPriceUSD(BigDecimal priceUSD) {
		this.priceUSD = priceUSD;
	}
	public void setPriceEUR(BigDecimal priceInEur) {
		this.priceEUR = priceInEur;
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

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public ProductDTO() {}

	@Override
	public String toString() {
		return "ProductDTO [name=" + name + ", code=" + code + ", priceEUR=" + priceEUR + ", priceUSD=" + priceUSD
				+ "]";
	}
	
	
	
	
	
	

}
