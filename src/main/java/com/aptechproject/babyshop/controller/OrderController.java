package com.aptechproject.babyshop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptechproject.babyshop.constant.AppConstants;
import com.aptechproject.babyshop.model.ErrorMessage;
import com.aptechproject.babyshop.service.OrderService;

@RestController
@RequestMapping(AppConstants.API_ORDERS)
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(AppConstants.API_HISTORY)
    public ResponseEntity<?> getMyOrderHistory() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(orderService.getOrderHistory(email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }
}
