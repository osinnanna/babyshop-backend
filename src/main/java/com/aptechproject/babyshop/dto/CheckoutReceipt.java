package com.aptechproject.babyshop.dto;

import lombok.Data;

@Data
public class CheckoutReceipt {
    private String message;
    private int totalItemsBought;
    private double totalAmountPaid;
}
