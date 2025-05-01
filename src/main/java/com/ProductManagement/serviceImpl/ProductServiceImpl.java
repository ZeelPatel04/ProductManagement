package com.ProductManagement.serviceImpl;

import com.ProductManagement.model.Product;
import com.ProductManagement.repository.ProductRepository;
import com.ProductManagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository repo;

    @Override
    public Product create(Product product) {
        return repo.save(product);
    }

    @Override
    public Product get(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Product update(String id, Product product) {
        if (!repo.existsById(id)) throw new RuntimeException("Product not found");
        product.setId(id);
        return repo.save(product);
    }

    @Override
    public void delete(String id) {
        repo.deleteById(id);
    }

    @Override
    public Page<Product> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repo.findAll(pageable);
    }
}
