package com.teamvoy.shop.service.impl;

import com.teamvoy.shop.dto.OrderDTO;
import com.teamvoy.shop.dto.OrderUpdateDTO;
import com.teamvoy.shop.entity.*;
import com.teamvoy.shop.entity.enums.Status;
import com.teamvoy.shop.mapper.OrderMapper;
import com.teamvoy.shop.repository.*;
import com.teamvoy.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {

    private final OrderPhoneRepository orderPhoneRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private final BasketPhoneRepository basketPhoneRepository;
    private final BasketRepository basketRepository;

    private final PhoneRepository phoneRepository;

    @Override
    public OrderDTO create(Long userId) {
        Basket basket = basketRepository.getByUserId(userId).orElse(null);
        if (basket == null) {
            throw new IllegalArgumentException("There are no items in the basket");
        }
        if (basket.getPhones().isEmpty()) {
            throw new IllegalArgumentException("There are no items in the basket");
        }
        basket.getPhones().stream()
                .filter(basketPhone -> basketPhone.getAmount() > basketPhone.getPhone().getAmount())
                .findFirst()
                .ifPresent(basketPhone -> {throw new IllegalArgumentException("There are no such goods in such quantity");});

        Order order = new Order().toBuilder()
                .user(basket.getUser())
                .status(Status.CREATED)
                .paid(false)
                .build();
        order = orderRepository.save(order);

        Long orderId = order.getId();

        order = order.toBuilder()
                .phones(basket.getPhones().stream()
                        .map(basketPhone -> basketPhoneToOrderPhone(basketPhone, orderId))
                        .collect(Collectors.toList()))
                .price(basket.getPrice())
                .build();
        order.setPhones(orderPhoneRepository.saveAll(order.getPhones()));
        order = orderRepository.save(order);

        order.getPhones().forEach(orderPhone ->
                orderPhone.getPhone().setAmount(orderPhone.getPhone().getAmount() - orderPhone.getAmount()));
        phoneRepository.saveAll(order.getPhones().stream().map(OrderPhone::getPhone).collect(Collectors.toList()));

        basketPhoneRepository.deleteAll(basket.getPhones());
        basket.setPhones(new ArrayList<>());
        basket.setPrice(0);
        basketRepository.save(basket);
        return orderMapper.toOrderDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getById(Long orderId) {
        return orderMapper.toOrderDTO(
                orderRepository.findById(orderId).orElseThrow()
        );
    }

    @Override
    public OrderDTO update(OrderUpdateDTO dto) {
        Order order = orderRepository.findById(dto.getId()).orElseThrow();
        order.setStatus(dto.getStatus());
        return orderMapper.toOrderDTO(
                orderRepository.save(order)
        );
    }

    @Override
    public boolean delete(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        orderPhoneRepository.deleteAll(order.getPhones());
        orderRepository.delete(order);//it is better to just throw this order in the archive to save the general statistics
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAll() {
        return orderMapper.toOrderDTO(
                orderRepository.findAll()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllByUser(Long userId) {
        return orderMapper.toOrderDTO(
                orderRepository.getAllByUser(userId)
        );
    }

    @Override
    public OrderDTO pay(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(Status.PAID);
        order.setPaid(true);
        orderRepository.save(order);
        return orderMapper.toOrderDTO(
                orderRepository.save(order)
        );
    }

    @Override
    public void checkIfPaid(Long orderId) {
        try {
            Thread.sleep(1 * 60 * 1000);
            Order order = orderRepository.findById(orderId).orElseThrow();
            if(!order.isPaid()) {
                delete(orderId);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static OrderPhone basketPhoneToOrderPhone(BasketPhone basketPhone, Long orderId) {
        return new OrderPhone().toBuilder()
                .id(new OrderPhonePK().toBuilder().orderId(orderId).phoneId(basketPhone.getPhone().getId()).build())
                .order(new Order().toBuilder().id(orderId).build())
                .phone(new Phone().toBuilder().id(basketPhone.getPhone().getId()).build())
                .amount(basketPhone.getAmount())
                .build();
    }
}
