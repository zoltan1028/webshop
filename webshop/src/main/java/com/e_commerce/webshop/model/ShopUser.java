package com.e_commerce.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.h2.engine.User;

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
    @Enumerated(EnumType.STRING)
    private UserRight userRight;
    private OffsetDateTime token_expire;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ShopOrder> shopOrderList = new ArrayList<>();
    public enum UserRight {
        ADMIN,
        USER;
    }
    public ShopUser() {}
    public ShopUser(String username, String password, UserRight userRight) {
        this.username = username;
        this.password = password;
        this.userRight = userRight;
    }
    public void addShopOrderToShopUser(ShopOrder order) {
        this.shopOrderList.add(order);
        order.setUser(this);
    }
    public void setToken(String token) {
        this.token = token;
        if(token != null) {
            OffsetDateTime timeIsNow = OffsetDateTime.now();
            this.token_expire = timeIsNow.plusSeconds(30);
        }
    }
}
