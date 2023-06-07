package com.example.springboogmichilli.controllers;

import com.example.springboogmichilli.dtos.ProductRecordDto;
import com.example.springboogmichilli.models.ProductModel;
import com.example.springboogmichilli.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final String NOT_FOUND = "Product not found";

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        List<ProductModel> productsList = productService.getAll();
        if (!productsList.isEmpty()) {
            for(ProductModel p: productsList) {
                p.add(linkTo(methodOn(ProductController.class).getById(p.getProductId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") UUID id) {
        Optional<ProductModel> product = productService.getById(id);
        if (product.isPresent()) {
            product.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Product list"));
            return ResponseEntity.status(HttpStatus.OK).body(product.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<ProductModel> createProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(productModel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(
            @PathVariable("id") UUID id,
            @RequestBody @Valid ProductRecordDto productRecordDto) {

        Optional<ProductModel> product = productService.getById(id);
        if (product.isPresent()) {
            var productModel = product.get();
            BeanUtils.copyProperties(productRecordDto, productModel);
            return ResponseEntity.status(HttpStatus.OK).body(productService.saveProduct(productModel));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") UUID id) {
        Optional<ProductModel> product = productService.getById(id);
        if(product.isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND);
        }
    }
}
