package com.e_commerce.webshop.service.APIService;

import com.e_commerce.webshop.dto.AuthUserDTO;
import com.e_commerce.webshop.model.ShopUser;
import com.e_commerce.webshop.repository.IUserRepository;
import com.e_commerce.webshop.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopUserService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    AuthenticationService authenticationService;
    public void saveUser(AuthUserDTO userDTO) {
        ShopUser newShopUser = new ShopUser();
        newShopUser.setUsername(userDTO.getUsername());
        newShopUser.setPassword(authenticationService.hashPassword(userDTO.getPassword()));
        newShopUser.setUserRight(ShopUser.UserRight.USER);
        userRepository.save(newShopUser);
    }

}
