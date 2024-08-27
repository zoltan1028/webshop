package com.e_commerce.webshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
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
    private OffsetDateTime token_expire;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ShopOrder> shopOrderList = new ArrayList<>();
    public void addShopOrderToShopUser(ShopOrder order) {
        this.shopOrderList.add(order);
        order.setUser(this);
    }
    public void setToken_expire() {
        //"2017-12-03T10:15:30+01:00" +1 created min
    }
    public void setToken(String token) {
        this.token = token;
        if(token != null) {
            OffsetDateTime timeIsNow = OffsetDateTime.now();
            this.token_expire = timeIsNow.plusSeconds(30);
        }
    }
}
