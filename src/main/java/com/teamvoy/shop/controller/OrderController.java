package com.teamvoy.shop.controller;

import com.teamvoy.shop.annotation.CurrentUser;
import com.teamvoy.shop.dto.OrderDTO;
import com.teamvoy.shop.dto.OrderUpdateDTO;
import com.teamvoy.shop.dto.PhoneDTO;
import com.teamvoy.shop.security.UserSecurity;
import com.teamvoy.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("orders")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDTO> getAllOrders(
            @CurrentUser UserSecurity userSecurity
    ) {
        return orderService.getAllByUser(userSecurity.getId());
    }

    @GetMapping("{id}")
    public OrderDTO getOrder(
            @PathVariable("id") Long orderId,
            @CurrentUser UserSecurity userSecurity
    ) {
        return orderService.getById(orderId, userSecurity.getId());
    }

    @PostMapping
    public OrderDTO create(
            @CurrentUser UserSecurity userSecurity
    ) {
        OrderDTO order = orderService.create(userSecurity.getId());
        Thread thread = new Thread(() -> orderService.checkIfPaid(order.getId()));
        thread.start();
        return order;
    }

    @PostMapping("{id}")
    public OrderDTO pay(
            @PathVariable("id") Long orderId,
            @CurrentUser UserSecurity userSecurity
    ) {
        return orderService.pay(orderId, userSecurity.getId());
    }

    @PutMapping
    public OrderDTO update(
            @Valid @RequestBody OrderUpdateDTO dto,
            @CurrentUser UserSecurity userSecurity
    ) {
        dto.setUserId(userSecurity.getId());
        return orderService.update(dto);
    }

    @DeleteMapping("{id}")
    public boolean delete(
            @PathVariable("id") Long orderId,
            @CurrentUser UserSecurity userSecurity
    ) {
        return orderService.delete(orderId, userSecurity.getId());
    }
}
