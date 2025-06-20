package com.e_commerce.webshop.service.persistence;

import com.e_commerce.webshop.dto.OrderProductDTO;
import com.e_commerce.webshop.dto.PaymentRequestDTO;
import com.e_commerce.webshop.model.Product;
import com.e_commerce.webshop.model.ProductQuantity;
import com.e_commerce.webshop.model.ShopOrder;
import com.e_commerce.webshop.model.ShopUser;
import com.e_commerce.webshop.repository.IProductQuantityRepository;
import com.e_commerce.webshop.repository.IProductRepository;
import com.e_commerce.webshop.repository.IShopOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    IShopOrderRepository shopOrderRepository;
    @Autowired
    IProductQuantityRepository productQuantityRepository;
    @Autowired
    IProductRepository productRepository;
    public void saveNewOrder(PaymentRequestDTO paymentRequest, ShopUser shopUser) {
        ShopOrder newOrder = new ShopOrder(shopUser);
        shopOrderRepository.save(newOrder);
        for (OrderProductDTO dtoItem : paymentRequest.getCart()) {
            Product productToSave = productRepository.findById(dtoItem.getId()).get();
            ProductQuantity quantity = new ProductQuantity(newOrder, productToSave, dtoItem.getPieces());
            newOrder.addProductQuantityToOrder(quantity);
            productQuantityRepository.save(quantity);
        }
    }
    public void modifyExistingOrder(PaymentRequestDTO paymentRequestDTO, ShopUser shopUser) {
        Long idOfSelectedOrder = paymentRequestDTO.getIdOfSelectedOrder();
        ShopOrder orderToModify = shopOrderRepository.findById(idOfSelectedOrder).get();
        List<ProductQuantity> productQuantities = orderToModify.getQuantityList();
        //Function.identity() maps value for Map
        Map<Long, ProductQuantity> pqs = productQuantities.stream()
                .collect(Collectors.toMap(
                        pq -> pq.getProduct().getId(),
                        Function.identity()));

        for (OrderProductDTO shoppingCartDTO : paymentRequestDTO.getCart()) {
            Long pid = shoppingCartDTO.getId();
            if (pqs.containsKey(pid)) {
                ProductQuantity pq = pqs.get(pid);
                pq.modifyOrderByQuantityChange(shoppingCartDTO.getPieces());
            } else {
                Product product = productRepository.findById(shoppingCartDTO.getId()).get();
                ProductQuantity quantity = new ProductQuantity(orderToModify, product, shoppingCartDTO.getPieces());
                orderToModify.addProductQuantityToOrder(quantity);
                productQuantityRepository.save(quantity);
            }
        }
    }
}
