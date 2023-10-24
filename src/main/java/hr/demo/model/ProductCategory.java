package hr.demo.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "product-category")
public class ProductCategory {
	 
	@Id
	@SequenceGenerator(name = "ingemark-seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingemark-seq")
    @Column(name = "category_id")
    public Long id;

    @Column(name = "category_name")
	@NotEmpty
    public String categoryName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private Set<Product> products;
    
    public void addProduct(Product product) {
		if (product != null) {
			if (products == null) {
				products = new HashSet<>();
			}
			
			products.add(product);
			product.setCategory(this);
		}
	}
    
    public ProductCategory(Long id) {
    	this.id = id;
    }
    
    public ProductCategory() {
    	
    }

	public ProductCategory(Long id, String categoryName) {
		this.id = id;
		this.categoryName = categoryName;
	}
   

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
 
	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

}
