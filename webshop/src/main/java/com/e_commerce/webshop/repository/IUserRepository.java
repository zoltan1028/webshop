package com.e_commerce.webshop.repository;

import com.e_commerce.webshop.model.ShopUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<ShopUser, Long> {
    Optional<ShopUser> findByUsername(String username);
    Optional<ShopUser> findByToken(String token);
}
