package com.example.springboogmichilli.services;

import com.example.springboogmichilli.models.ProductModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

    public ProductModel saveProduct(ProductModel product);

    public List<ProductModel> getAll();

    public Optional<ProductModel> getById(UUID id);

    public void deleteProduct(UUID id);
}
