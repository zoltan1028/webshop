package com.e_commerce.webshop.dto.ordersbyuser;

import com.e_commerce.webshop.model.ShopOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrdersByUserShopOrderDTO {
    private Long id;
    private List<OrdersByUserProductQuantityDTO> quantityList = new ArrayList<>();
    private ShopOrder.OrderStatus status;
    private OffsetDateTime orderReceivedAt;
    private BigDecimal orderTotal;
}
