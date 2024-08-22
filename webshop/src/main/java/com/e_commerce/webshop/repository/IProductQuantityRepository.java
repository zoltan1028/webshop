package com.e_commerce.webshop.repository;

import com.e_commerce.webshop.model.ProductQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductQuantityRepository extends JpaRepository<ProductQuantity, Long> {
}
