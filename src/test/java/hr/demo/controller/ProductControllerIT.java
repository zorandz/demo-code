package hr.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.demo.dto.ProductDTO;
import hr.demo.exceptions.ProductNotFoundException;
import hr.demo.junitextension.TimingExtension;
import hr.demo.service.ProductService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerIT {

    private ProductDTO productDTO;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private ProductController productController;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
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
    public void ProductController_updateProduct_returnStatusOKAndUpdatedProduct() throws Exception {
        ProductDTO updatedProduct = new ProductDTO("1234567890",
                "product_1",
                BigDecimal.valueOf(250),
                BigDecimal.valueOf(270),
                true,
                "promijenjen",
                "1");

        given(productService.updateProduct(productDTO.getCode(), productDTO.getName(), productDTO.getPriceEUR(), productDTO.getDescription(), productDTO.getIsAvailable()))
                .willReturn(updatedProduct);

        ResultActions response = mockMvc.perform(put("/products/update-product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedProduct)));
    }

    @Test
    public void ProductController_createProduct_returnResponseEntityAndStatusIsCreated() throws Exception {
        given(productService.createProduct(productDTO.getName(), productDTO.getPriceEUR(), productDTO.getDescription(), productDTO.getIsAvailable(), productDTO.getCategoryId()))
                .willAnswer(new Answer<ProductDTO>() {
                    @Override
                    public ProductDTO answer(InvocationOnMock invocation) throws Throwable {
                        Object[] arguments = invocation.getArguments();
                        String name = (String) arguments[0];
                        BigDecimal price = (BigDecimal) arguments[1];
                        String description = (String) arguments[2];
                        Boolean isAvailable = (Boolean) arguments[3];
                        String categoryId = (String) arguments[4];

                        return new ProductDTO(name, price, description, isAvailable, categoryId);
                    }
                });

        ResultActions response = mockMvc.perform(post("/products/add-product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)));

        response.andExpect(MockMvcResultMatchers.jsonPath("name").value("product_1"));
        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void ProductController_createProduct_returnNameCanNotBeEmpty() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setName(null);
        product.setIsAvailable(true);
        product.setPriceEUR(BigDecimal.valueOf(10));
        product.setCategoryId("1");
        product.setDescription("desc");

        mockMvc.perform(post("/products/add-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").value("Name can not be empty"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void ProductController_createProduct_returnPriceMustBeGreaterThanZero() throws Exception {
        this.productDTO.setPriceEUR(BigDecimal.valueOf(0));

        mockMvc.perform(post("/products/add-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").value("Price must be greater than zero"));
    }

    @Test
    public void ProductController_createProduct_returnDescriptionCanNotBeEmpty() throws Exception {
        productDTO.setDescription(null);

        mockMvc.perform(post("/products/add-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").value("Description can not be empty"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void ProductController_findAll_returnsFirstPageWithFourProducts() throws Exception {
        List<ProductDTO> productDTOs = new ArrayList<>();
        productDTOs.add(new ProductDTO());
        productDTOs.add(new ProductDTO());
        productDTOs.add(new ProductDTO());
        productDTOs.add(new ProductDTO());

        Pageable pageable = PageRequest.of(0, 4);

        Page<ProductDTO> pageOfProducts = new PageImpl<>(productDTOs, pageable, productDTOs.size());

        given(productService.findAll(PageRequest.of(0, 4))).willReturn(pageOfProducts);

            mockMvc.perform(get("/products?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()", CoreMatchers.is(pageOfProducts.getContent().size())))
                    .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void ProductController_getProductDetails_returnProductDTO() throws Exception {

        when(productService.getProductByCode(productDTO.getCode())).thenReturn(productDTO);

        ResultActions response = mockMvc.perform(get("/products/product/" + productDTO.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("product_1"));
    }

    @Test
    public void ProductController_getProductDetails_returnInternalServerError() throws Exception {

        when(productService.getProductByCode(productDTO.getCode())).thenThrow(new RuntimeException("An error occurred while getting the product."));

        ResultActions response = mockMvc.perform(get("/products/product/" + productDTO.getCode())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isInternalServerError());
        response.andExpect(MockMvcResultMatchers.content().string(containsString("Internal server error")));
    }

    @Test
    public void ProductController_getProductDetails_returnNotFound() throws Exception {

        when(productService.getProductByCode(productDTO.getCode())).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/products/product/" + productDTO.getCode())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void ProductController_deleteProduct_returnStatusOK() throws Exception {

        doNothing().when(productService).deleteProductByCode(productDTO.getCode());

        ResultActions response = mockMvc.perform(delete("/products/delete-product/" + productDTO.getCode())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void ProductController_deleteProduct_returnStatusNotFound() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw new ProductNotFoundException("Product not found");
            }
        }).when(productService).deleteProductByCode(anyString());

        ResultActions response = mockMvc.perform(delete("/products/delete-product/" + productDTO.getCode())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void ProductController_deleteProduct_returnStatusInternalServerError() throws Exception {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw new Exception("Server Error");
            }
        }).when(productService).deleteProductByCode(anyString());

        ResultActions response = mockMvc.perform(delete("/products/delete-product/" + productDTO.getCode())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
