package com.e_commerce.webshop.repository;

import com.e_commerce.webshop.model.Product;
import com.e_commerce.webshop.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory_CategoryEnum(ProductCategory.ShopProductCategory category, Pageable pageable);
}
