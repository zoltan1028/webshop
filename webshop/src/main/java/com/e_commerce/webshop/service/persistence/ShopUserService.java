package com.e_commerce.webshop.service.persistence;

import com.e_commerce.webshop.dto.AuthLoginDTO;
import com.e_commerce.webshop.model.ShopUser;
import com.e_commerce.webshop.repository.IUserRepository;
import com.e_commerce.webshop.service.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopUserService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    AuthenticationService authenticationService;
    public void saveUser(AuthLoginDTO userDTO) {
        ShopUser newShopUser = new ShopUser(userDTO.getUsername(), authenticationService.hashPassword(userDTO.getPassword()), ShopUser.UserRight.USER);
        userRepository.save(newShopUser);
    }
}
