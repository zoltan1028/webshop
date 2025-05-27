package com.e_commerce.webshop.dto.ordersbyuser;

import com.e_commerce.webshop.model.ProductQuantity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdersByUserProductQuantityDTO {
    private Long id;
    private int sum;
    private int quantity;
    private OrdersByUserProductDTO product;
    public OrdersByUserProductQuantityDTO(ProductQuantity productQuantity) {
        this.id = productQuantity.getId();
        this.sum = productQuantity.getSum();
        this.quantity = productQuantity.getQuantity();
        this.product = new OrdersByUserProductDTO(productQuantity.getProduct());
    }
}
