package com.e_commerce.webshop.dto.ordersbyuser;


import com.e_commerce.webshop.model.Product;
import com.e_commerce.webshop.model.ProductQuantity;
import com.e_commerce.webshop.model.ShopOrder;
import com.e_commerce.webshop.model.ShopUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrdersByUserMapper {
    OrdersByUserUserDTO userToDto(ShopUser user);
    OrdersByUserShopOrderDTO orderToDto(ShopOrder order);
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    OrdersByUserProductQuantityDTO quantityToDto(ProductQuantity quantity);
}
