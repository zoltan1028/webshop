package com.e_commerce.webshop.repository;

import com.e_commerce.webshop.model.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IShopOrderRepository extends JpaRepository<ShopOrder, Long> {

}
