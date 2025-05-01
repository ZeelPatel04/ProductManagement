package com.ProductManagement.service;

import com.ProductManagement.controller.ProductController;
import com.ProductManagement.model.Product;
import com.ProductManagement.repository.ProductRepository;
import com.ProductManagement.serviceImpl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@WebMvcTest(ProductController.class)
class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product("1", "Phone", "Smartphone", new BigDecimal("500.00"), 10);
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(product)).thenReturn(product);

        Product createdProduct = productService.create(product);
        assertNotNull(createdProduct);
        assertEquals("Phone", createdProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        Product foundProduct = productService.get("1");
        assertNotNull(foundProduct);
        assertEquals("Phone", foundProduct.getName());
        verify(productRepository, times(1)).findById("1");
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById("2")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> productService.get("2"));
        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, times(1)).findById("2");
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.existsById("1")).thenReturn(true);
        when(productRepository.save(product)).thenReturn(product);

        Product updatedProduct = productService.update("1", product);
        assertNotNull(updatedProduct);
        assertEquals("Phone", updatedProduct.getName());
        verify(productRepository, times(1)).existsById("1");
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProductNotFound() {
        when(productRepository.existsById("2")).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> productService.update("2", product));
        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, times(1)).existsById("2");
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById("1");

        productService.delete("1");
        verify(productRepository, times(1)).deleteById("1");
    }

    @Test
    void testGetAllProducts() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<Product> result = productService.getAll(0, 10, "name");
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Phone", result.getContent().get(0).getName());
        verify(productRepository, times(1)).findAll(pageable);
    }
}