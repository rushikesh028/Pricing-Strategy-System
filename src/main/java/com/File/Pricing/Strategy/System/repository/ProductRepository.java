package com.File.Pricing.Strategy.System.repository;

import com.File.Pricing.Strategy.System.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameIgnoreCase(String name);

}
