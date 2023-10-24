package hr.demo.utility;

import hr.demo.dto.ProductDTO;
import hr.demo.model.Product;
import org.springframework.stereotype.Component;

public interface ProductMapper {
    default ProductDTO map(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.getName());
        productDTO.setCode(product.getCode());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategoryId(product.getCategory().getId().toString());
        productDTO.setIsAvailable(product.getIsAvailable());
        productDTO.setPriceEUR(product.getPriceEur());
        productDTO.setPriceUSD(product.getPriceUsd());
        return productDTO;
    }
}
