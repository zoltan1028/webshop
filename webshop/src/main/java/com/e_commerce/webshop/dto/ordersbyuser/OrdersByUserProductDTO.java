package com.e_commerce.webshop.dto.ordersbyuser;

import com.e_commerce.webshop.model.Product;
import com.e_commerce.webshop.model.ProductCategory;
import com.e_commerce.webshop.model.ProductQuantity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrdersByUserProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    public OrdersByUserProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
    }
}
