package com.e_commerce.webshop.dto;
import com.e_commerce.webshop.model.ProductCategory;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class NewProductDTO {
    private String name;
    private BigDecimal price;
    private int stock;
    private String description;
    private String picture;
    private String category;
}