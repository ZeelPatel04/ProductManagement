package com.ProductManagement.service;

import com.ProductManagement.model.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
    Product create(Product product);
    Product get(String id);
    Product update(String id, Product product);
    void delete(String id);
    Page<Product> getAll(int page, int size, String sortBy);
}
