package com.teamvoy.shop.service;

import com.teamvoy.shop.dto.OrderDTO;
import com.teamvoy.shop.dto.OrderUpdateDTO;
import com.teamvoy.shop.dto.PhoneDTOWithOtherInfo;
import com.teamvoy.shop.dto.UserDTO;
import com.teamvoy.shop.entity.*;
import com.teamvoy.shop.entity.enums.Role;
import com.teamvoy.shop.entity.enums.Status;
import com.teamvoy.shop.mapper.OrderMapper;
import com.teamvoy.shop.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderPhoneRepository orderPhoneRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderMapper orderMapper;

    @MockBean
    private BasketRepository basketRepository;

    @MockBean
    private PhoneRepository phoneRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void create() {
        User user = new User().toBuilder()
                .id(2L)
                .surname("Андрій")
                .name("Татарчук")
                .email("test2@gmail.com")
                .phone("380972553992")
                .build();

        Phone phone = new Phone().toBuilder()
                .id(4L)
                .brand("Apple")
                .model("iPhone X")
                .price(300)
                .amount(10)
                .color("Space grey")
                .description("Nice phone")
                .build();

        BasketPhone basketPhone = new BasketPhone().toBuilder()
                .id(new BasketPhonePK().toBuilder().basketId(1L).phoneId(phone.getId()).build())
                .basket(new Basket().toBuilder().id(1L).build())
                .phone(phone)
                .amount(3)
                .build();


        Basket basket = new Basket().toBuilder()
                .id(1L)
                .user(user)
                .phones((List.of(basketPhone)))
                .price(300)
                .build();

        Order order = new Order().toBuilder()
                .user(user)
                .status(Status.CREATED)
                .paid(false)
                .build();

        Order saveOrder = new Order().toBuilder()
                .id(1L)
                .user(user)
                .status(Status.CREATED)
                .paid(false)
                .build();

        OrderDTO expected = new OrderDTO().toBuilder()
                .id(1L)
                .user(new UserDTO().toBuilder()
                        .id(2L)
                        .surname("Андрій")
                        .name("Татарчук")
                        .email("test2@gmail.com")
                        .phone("380972553992")
                        .build())
                .phones((List.of(new PhoneDTOWithOtherInfo().toBuilder()
                        .id(phone.getId())
                        .brand(phone.getBrand())
                        .model(phone.getModel())
                        .price(phone.getPrice())
                        .amount(phone.getAmount())
                        .color(phone.getColor())
                        .description(phone.getDescription())
                        .amount(basketPhone.getAmount())
                        .build())))
                .status(Status.CREATED)
                .price(900)
                .paid(false)
                .build();

        when(basketRepository.getByUserId(basket.getUser().getId())).thenReturn(Optional.of(basket));
        when(orderRepository.save(order)).thenReturn(saveOrder);

        saveOrder.setPhones(List.of(new OrderPhone().toBuilder()
                .id(new OrderPhonePK().toBuilder().orderId(1L).phoneId(4L).build())
                .order(new Order().toBuilder().id(1L).build())
                .phone(phone)
                .amount(3)
                .build()));
        saveOrder.setPrice(basket.getPrice());

        when(orderPhoneRepository.saveAll(saveOrder.getPhones())).thenReturn(saveOrder.getPhones());
        when(orderRepository.save(saveOrder)).thenReturn(saveOrder);

        phone.setAmount(phone.getAmount() - basketPhone.getAmount());

        when(phoneRepository.saveAll(List.of(phone))).thenReturn(List.of(phone));

        basket.setPrice(0);

        when(basketRepository.save(basket)).thenReturn(basket);
        when(orderMapper.toOrderDTO(saveOrder)).thenReturn(expected);

        assertEquals(expected, orderService.create(user.getId()));

        verify(basketRepository, times(1)).getByUserId(user.getId());
        verify(orderRepository, times(1)).save(order);
        verify(orderPhoneRepository, times(1)).saveAll(saveOrder.getPhones());
        verify(orderRepository, times(1)).save(saveOrder);
        verify(phoneRepository, times(1)).saveAll(List.of(phone));
        verify(basketRepository, times(1)).save(basket);
        verify(orderMapper, times(1)).toOrderDTO(saveOrder);
    }

    @Test
    void getById() {
        User user = new User().toBuilder()
                .id(2L)
                .surname("Андрій")
                .name("Татарчук")
                .email("test2@gmail.com")
                .phone("380972553992")
                .roles(List.of(Role.ROLE_USER, Role.ROLE_MANAGER))
                .build();

        Phone phone = new Phone().toBuilder()
                .id(4L)
                .brand("Apple")
                .model("iPhone X")
                .price(300)
                .amount(10)
                .color("Space grey")
                .description("Nice phone")
                .build();

        Order order = new Order().toBuilder()
                .id(1L)
                .user(user)
                .phones(List.of(new OrderPhone().toBuilder()
                        .id(new OrderPhonePK().toBuilder().orderId(1L).phoneId(phone.getId()).build())
                        .order(new Order().toBuilder().id(1L).build())
                        .phone(phone)
                        .amount(3)
                        .build()))
                .status(Status.CREATED)
                .price(900)
                .paid(false)
                .build();

        OrderDTO expected = new OrderDTO().toBuilder()
                .id(order.getId())
                .user(new UserDTO().toBuilder()
                        .id(user.getId())
                        .surname(user.getSurname())
                        .name(user.getName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .build())
                .phones((List.of(new PhoneDTOWithOtherInfo().toBuilder()
                        .id(phone.getId())
                        .brand(phone.getBrand())
                        .model(phone.getModel())
                        .price(phone.getPrice())
                        .amount(phone.getAmount())
                        .color(phone.getColor())
                        .description(phone.getDescription())
                        .amount(3)
                        .build())))
                .status(order.getStatus())
                .price(order.getPrice())
                .paid(order.isPaid())
                .build();

        when(orderRepository.getByOrderIdAndUserId(order.getId(), user.getId())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderMapper.toOrderDTO(order)).thenReturn(expected);

        assertEquals(expected, orderService.getById(order.getId(), user.getId()));

        verify(orderRepository, times(1)).getByOrderIdAndUserId(order.getId(), user.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderMapper, times(1)).toOrderDTO(order);
    }

    @Test
    void update() {
        User user = new User().toBuilder()
                .id(2L)
                .surname("Андрій")
                .name("Татарчук")
                .email("test2@gmail.com")
                .phone("380972553992")
                .roles(List.of(Role.ROLE_USER, Role.ROLE_MANAGER))
                .build();

        Phone phone = new Phone().toBuilder()
                .id(4L)
                .brand("Apple")
                .model("iPhone X")
                .price(300)
                .amount(10)
                .color("Space grey")
                .description("Nice phone")
                .build();

        Order order = new Order().toBuilder()
                .id(1L)
                .user(user)
                .phones(List.of(new OrderPhone().toBuilder()
                        .id(new OrderPhonePK().toBuilder().orderId(1L).phoneId(phone.getId()).build())
                        .order(new Order().toBuilder().id(1L).build())
                        .phone(phone)
                        .amount(3)
                        .build()))
                .status(Status.CREATED)
                .price(900)
                .paid(false)
                .build();

        OrderUpdateDTO dto = new OrderUpdateDTO().toBuilder()
                .id(order.getId())
                .userId(user.getId())
                .status(Status.ARCHIVE)
                .build();

        OrderDTO expected = new OrderDTO().toBuilder()
                .id(order.getId())
                .user(new UserDTO().toBuilder()
                        .id(user.getId())
                        .surname(user.getSurname())
                        .name(user.getName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .build())
                .phones((List.of(new PhoneDTOWithOtherInfo().toBuilder()
                        .id(phone.getId())
                        .brand(phone.getBrand())
                        .model(phone.getModel())
                        .price(phone.getPrice())
                        .amount(phone.getAmount())
                        .color(phone.getColor())
                        .description(phone.getDescription())
                        .amount(3)
                        .build())))
                .status(dto.getStatus())
                .price(order.getPrice())
                .paid(order.isPaid())
                .build();

        when(orderRepository.getByOrderIdAndUserId(order.getId(), user.getId())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toOrderDTO(order)).thenReturn(expected);

        assertEquals(expected, orderService.update(dto));

        verify(orderRepository, times(1)).getByOrderIdAndUserId(order.getId(), user.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(order);
        verify(orderMapper, times(1)).toOrderDTO(order);
    }

    @Test
    void pay() {
        User user = new User().toBuilder()
                .id(2L)
                .surname("Андрій")
                .name("Татарчук")
                .email("test2@gmail.com")
                .phone("380972553992")
                .roles(List.of(Role.ROLE_USER, Role.ROLE_MANAGER))
                .build();

        Phone phone = new Phone().toBuilder()
                .id(4L)
                .brand("Apple")
                .model("iPhone X")
                .price(300)
                .amount(10)
                .color("Space grey")
                .description("Nice phone")
                .build();

        Order order = new Order().toBuilder()
                .id(1L)
                .user(user)
                .phones(List.of(new OrderPhone().toBuilder()
                        .id(new OrderPhonePK().toBuilder().orderId(1L).phoneId(phone.getId()).build())
                        .order(new Order().toBuilder().id(1L).build())
                        .phone(phone)
                        .amount(3)
                        .build()))
                .status(Status.CREATED)
                .price(900)
                .paid(false)
                .build();

        when(orderRepository.getByOrderIdAndUserId(order.getId(), user.getId())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        order.setStatus(Status.PAID);
        order.setPaid(true);

        when(orderRepository.save(order)).thenReturn(order);

        OrderDTO expected = new OrderDTO().toBuilder()
                .id(order.getId())
                .user(new UserDTO().toBuilder()
                        .id(user.getId())
                        .surname(user.getSurname())
                        .name(user.getName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .build())
                .phones((List.of(new PhoneDTOWithOtherInfo().toBuilder()
                        .id(phone.getId())
                        .brand(phone.getBrand())
                        .model(phone.getModel())
                        .price(phone.getPrice())
                        .amount(phone.getAmount())
                        .color(phone.getColor())
                        .description(phone.getDescription())
                        .amount(3)
                        .build())))
                .status(order.getStatus())
                .price(order.getPrice())
                .paid(order.isPaid())
                .build();

        when(orderMapper.toOrderDTO(order)).thenReturn(expected);

        assertEquals(expected, orderService.pay(order.getId(), user.getId()));
        assertEquals(expected.getStatus(), Status.PAID);
        assertTrue(expected.isPaid());

        verify(orderRepository, times(1)).getByOrderIdAndUserId(order.getId(), user.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(order);
        verify(orderMapper, times(1)).toOrderDTO(order);
    }
}