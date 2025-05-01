package com.ProductManagement.controller;

import com.ProductManagement.model.Product;
import com.ProductManagement.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setup() {
        sampleProduct = new Product("1", "A", "Sample Description", new BigDecimal("8000.00"), 800);
    }

    @Test
    void shouldReturnAllProducts() throws Exception {
        Page<Product> mockPage = new PageImpl<>(List.of(sampleProduct));
        when(productService.getAll(0, 10, "name")).thenReturn(mockPage);

        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("A"))
                .andExpect(jsonPath("$.content[0].price").value(8000.00))
                .andExpect(jsonPath("$.content[0].quantity").value(800));

    }

    @Test
    void shouldReturnProductById() throws Exception {
        when(productService.get("1")).thenReturn(sampleProduct);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Phone"))
                .andExpect(jsonPath("$.description").value("Android Phone"));
    }

    @Test
    void shouldReturnNotFoundIfProductDoesNotExist() throws Exception {
        when(productService.get("2")).thenReturn(null);

        mockMvc.perform(get("/api/products/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteProductById() throws Exception {
        doNothing().when(productService).delete("1");

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk());

        verify(productService, times(1)).delete("1");
    }
}
