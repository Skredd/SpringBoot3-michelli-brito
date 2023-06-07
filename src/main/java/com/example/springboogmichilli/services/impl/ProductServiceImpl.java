package com.example.springboogmichilli.services.impl;

import com.example.springboogmichilli.models.ProductModel;
import com.example.springboogmichilli.repositories.ProductRepository;
import com.example.springboogmichilli.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductModel> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<ProductModel> getById(UUID id) {
        return productRepository.findById(id);
    }

    @Override
    public ProductModel saveProduct(ProductModel product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}
