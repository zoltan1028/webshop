package com.e_commerce.webshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private String name;
    private int pieces;
}
