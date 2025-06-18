package com.e_commerce.webshop.repository;

import com.e_commerce.webshop.model.ProductQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductQuantityRepository extends JpaRepository<ProductQuantity, Long> {
    List<ProductQuantity> findByShoporderId(Long id);
}
