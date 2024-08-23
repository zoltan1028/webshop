package com.e_commerce.webshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity
public class ShopOrder {
    public void addProductQuantityToOrder(ProductQuantity pq) {
        this.quantityList.add(pq);
        pq.setShoporder(this);
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserorderId")
    private ShopUser user;
    private String status;
}
