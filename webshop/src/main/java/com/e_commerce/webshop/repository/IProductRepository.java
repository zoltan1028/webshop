package com.e_commerce.webshop.repository;

import com.e_commerce.webshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductRepository extends JpaRepository<Product, Long> {
}
