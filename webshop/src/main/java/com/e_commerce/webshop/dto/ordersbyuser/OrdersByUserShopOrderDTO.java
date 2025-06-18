package com.e_commerce.webshop.dto.ordersbyuser;

import com.e_commerce.webshop.model.ShopOrder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrdersByUserShopOrderDTO {
    private Long id;
    private List<OrdersByUserProductQuantityDTO> quantityList = new ArrayList<>();
    private ShopOrder.OrderStatus status;
    private OffsetDateTime orderReceivedAt;
    private BigDecimal orderTotal;
    public OrdersByUserShopOrderDTO(ShopOrder order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.orderTotal = order.getOrderTotal();
        this.orderReceivedAt = order.getOrderReceivedAt();
        List<OrdersByUserProductQuantityDTO> quantityDTOList = new ArrayList<>();
        for (var q: order.getQuantityList()) {
            quantityDTOList.add(new OrdersByUserProductQuantityDTO(q));
        }
        this.quantityList = quantityDTOList;
    }
}
