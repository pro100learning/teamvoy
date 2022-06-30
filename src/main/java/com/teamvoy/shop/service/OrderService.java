package com.teamvoy.shop.service;

import com.teamvoy.shop.dto.OrderDTO;
import com.teamvoy.shop.dto.OrderUpdateDTO;

import java.util.List;

public interface OrderService {

    OrderDTO create(Long userId);

    OrderDTO getById(Long orderId, Long userId);

    OrderDTO update(OrderUpdateDTO dto);

    boolean delete(Long orderId, Long userId);

    List<OrderDTO> getAll();


    List<OrderDTO> getAllByUser(Long userId);

    OrderDTO pay(Long orderId, Long userId);

    void checkIfPaid(Long orderId);
}
