package com.File.Pricing.Strategy.System.config;

import com.File.Pricing.Strategy.System.model.Product;
import com.File.Pricing.Strategy.System.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public ProductDataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        List<Product> sampleProducts = List.of(
                new Product(null, "Laptop Pro 14", "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=800&q=80", 89999, 78, 87999, 42),
                new Product(null, "Smartphone X", "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=800&q=80", 55999, 84, 54999, 55),
                new Product(null, "Wireless Headphones", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=800&q=80", 7999, 72, 7499, 68),
                new Product(null, "Smart Watch S", "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=800&q=80", 12999, 66, 12499, 33),
                new Product(null, "Gaming Keyboard", "https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?auto=format&fit=crop&w=800&q=80", 4999, 61, 4799, 74),
                new Product(null, "4K Monitor 27", "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?auto=format&fit=crop&w=800&q=80", 24999, 58, 23999, 25),
                new Product(null, "Bluetooth Speaker", "https://images.unsplash.com/photo-1589003077984-894e133dabab?auto=format&fit=crop&w=800&q=80", 3499, 75, 3299, 96),
                new Product(null, "Portable SSD 1TB", "https://images.unsplash.com/photo-1591488320449-011701bb6704?auto=format&fit=crop&w=800&q=80", 8999, 70, 8599, 47)
        );

        List<Product> toInsert = new ArrayList<>();
        for (Product product : sampleProducts) {
            if (!productRepository.existsByNameIgnoreCase(product.getName())) {
                toInsert.add(product);
            }
        }

        if (!toInsert.isEmpty()) {
            productRepository.saveAll(toInsert);
        }
    }
}
