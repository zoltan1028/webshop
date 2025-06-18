package com.e_commerce.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity
public class ShopOrder {
    public ShopOrder() {}
    public ShopOrder(ShopUser user) {
        this.orderReceivedAt = OffsetDateTime.now();
        this.status = OrderStatus.PENDING;
        this.user = user;
    }
    public enum OrderStatus {
        PENDING,
        COMPLETED;
    }
    public void addProductQuantityToOrder(ProductQuantity pq) {
        this.quantityList.add(pq);
        pq.setShoporder(this);
        this.orderTotal = this.orderTotal.add(pq.getSum());
    }
    public void removeProductQuantityFromOrder(ProductQuantity pq) {
        this.quantityList.remove(pq);
        pq.setShoporder(null);
    }
    @GeneratedValue
    @Id
    private Long id;
    @OneToMany(mappedBy = "shoporder", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductQuantity> quantityList = new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserorderId")
    private ShopUser user;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private OffsetDateTime orderReceivedAt;
    private BigDecimal orderTotal = BigDecimal.ZERO;;

    public void  setOrderTotal() {

    }
}
