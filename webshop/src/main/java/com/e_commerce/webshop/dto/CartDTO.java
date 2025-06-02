package com.e_commerce.webshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDTO {
    private List<OrderProductDTO> list;
}
