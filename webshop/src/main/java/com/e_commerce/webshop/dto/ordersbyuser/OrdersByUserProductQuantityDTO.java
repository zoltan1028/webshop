package com.e_commerce.webshop.dto.ordersbyuser;

import com.e_commerce.webshop.model.ProductQuantity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrdersByUserProductQuantityDTO {
    private Long id;
    private BigDecimal sum;
    private int quantity;
    //flattend Product
    private String name;
    private BigDecimal price;
}
