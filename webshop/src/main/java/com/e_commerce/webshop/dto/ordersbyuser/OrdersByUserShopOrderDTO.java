package com.e_commerce.webshop.dto.ordersbyuser;

import com.e_commerce.webshop.model.ShopOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrdersByUserShopOrderDTO {
    private Long id;
    private List<OrdersByUserProductQuantityDTO> quantityList = new ArrayList<>();
    private String status;
    public OrdersByUserShopOrderDTO(ShopOrder order) {
        this.id = order.getId();
        this.status = order.getStatus();
        List<OrdersByUserProductQuantityDTO> quantityDTOList = new ArrayList<>();
        for (var q: order.getQuantityList()) {
            quantityDTOList.add(new OrdersByUserProductQuantityDTO(q));
        }
        this.quantityList = quantityDTOList;
    }
}
