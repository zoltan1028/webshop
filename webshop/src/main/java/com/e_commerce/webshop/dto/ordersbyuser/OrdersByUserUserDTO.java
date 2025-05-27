package com.e_commerce.webshop.dto.ordersbyuser;
import com.e_commerce.webshop.model.ShopUser;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrdersByUserUserDTO {
    private Long id;
    private String username;
    private List<OrdersByUserShopOrderDTO> shopOrderList = new ArrayList<>();
    public OrdersByUserUserDTO(ShopUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        List<OrdersByUserShopOrderDTO> orders = new ArrayList<>();
        for (var o: user.getShopOrderList()) {
            orders.add(new OrdersByUserShopOrderDTO(o));
        }
        this.shopOrderList = orders;
    }
}
