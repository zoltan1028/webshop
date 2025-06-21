package com.e_commerce.webshop.dto.ordersbyuser;
import com.e_commerce.webshop.model.ShopUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrdersByUserUserDTO {
    private Long id;
    private String username;
    private List<OrdersByUserShopOrderDTO> shopOrderList;
}
