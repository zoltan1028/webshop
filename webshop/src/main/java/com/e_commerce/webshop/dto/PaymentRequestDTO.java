package com.e_commerce.webshop.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentRequestDTO {
    //flexible untyped node
    private JsonNode google_tokenData;
    private List<OrderProductDTO> cart;
    public String getGoogleTokenAsJsonString() {
        return google_tokenData.toString();
    }
}
