package com.File.Pricing.Strategy.System.service;

import com.File.Pricing.Strategy.System.model.Product;
import com.File.Pricing.Strategy.System.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // CREATE
    public Product addProduct(Product product){
        return productRepository.save(product);
    }

    // READ ALL
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    // READ BY ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // UPDATE
    public Product updateProduct(Long id, Product newProduct) {
        Product product = getProductById(id); // reuse method

        product.setName(newProduct.getName());
        product.setImageUrl(newProduct.getImageUrl());
        product.setBasePrice(newProduct.getBasePrice());
        product.setDemand(newProduct.getDemand());
        product.setCompetitorPrice(newProduct.getCompetitorPrice());
        product.setStock(newProduct.getStock());

        return productRepository.save(product);
    }

    // DELETE
    public void deleteProduct(Long id) {
        Product product = getProductById(id); // ensures exists
        productRepository.delete(product);
    }

}
