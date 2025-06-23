package com.e_commerce.webshop.repository;

import com.e_commerce.webshop.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<ProductCategory, Long> {
    ProductCategory findByCategoryEnum(ProductCategory.ShopProductCategory category);
}
