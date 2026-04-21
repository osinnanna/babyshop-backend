package com.aptechproject.babyshop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aptechproject.babyshop.constant.AppConstants;
import com.aptechproject.babyshop.dto.OrderHistoryItemResponse;
import com.aptechproject.babyshop.dto.OrderHistoryResponse;
import com.aptechproject.babyshop.model.Order;
import com.aptechproject.babyshop.model.OrderItem;
import com.aptechproject.babyshop.model.User;
import com.aptechproject.babyshop.repository.OrderRepository;
import com.aptechproject.babyshop.repository.UserRepository;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderHistoryResponse> getOrderHistory(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException(AppConstants.ERROR_INVALID_CREDENTIALS));

        return orderRepository.findByUserOrderByCreatedAtDesc(user)
            .stream()
            .map(this::mapOrder)
            .toList();
    }

    private OrderHistoryResponse mapOrder(Order order) {
        OrderHistoryResponse response = new OrderHistoryResponse();
        response.setOrderId(order.getId());
        response.setCreatedAt(order.getCreatedAt());
        response.setTotalItems(order.getTotalItems());
        response.setTotalAmount(order.getTotalAmount());
        response.setItems(order.getItems().stream().map(this::mapOrderItem).toList());
        return response;
    }

    private OrderHistoryItemResponse mapOrderItem(OrderItem item) {
        OrderHistoryItemResponse response = new OrderHistoryItemResponse();
        response.setProductId(item.getProductId());
        response.setProductName(item.getProductName());
        response.setProductImageUrl(item.getProductImageUrl());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setLineTotal(item.getLineTotal());
        return response;
    }
}
