package com.e_commerce.webshop.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewProductDTO {
    private String name;
    private int price;
    private int stock;
    private String description;
    private String picture;
}