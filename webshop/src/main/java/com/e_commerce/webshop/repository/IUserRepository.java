package com.e_commerce.webshop.repository;

import com.e_commerce.webshop.model.ShopUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<ShopUser, Long> {
}
