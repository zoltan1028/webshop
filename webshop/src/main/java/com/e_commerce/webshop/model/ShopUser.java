package com.e_commerce.webshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class ShopUser {
    @GeneratedValue
    @Id
    private Long id;
    private String username;
    private String password;
    private String token;
    private String admin;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ShopOrder> shopOrderList = new ArrayList<>();
    public void addShopOrderToShopUser(ShopOrder order) {
        this.shopOrderList.add(order);
        order.setUser(this);
    }
}
