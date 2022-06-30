package com.teamvoy.shop.service;

import com.teamvoy.shop.dto.BasketDTO;
import com.teamvoy.shop.dto.BasketUpdateDTO;
import com.teamvoy.shop.dto.PhoneDTOWithOtherInfo;
import com.teamvoy.shop.dto.UserDTO;
import com.teamvoy.shop.entity.*;
import com.teamvoy.shop.mapper.BasketMapper;
import com.teamvoy.shop.repository.BasketPhoneRepository;
import com.teamvoy.shop.repository.BasketRepository;
import com.teamvoy.shop.repository.PhoneRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class BasketServiceTest {

    @Autowired
    private BasketService basketService;

    @MockBean
    private BasketPhoneRepository basketPhoneRepository;

    @MockBean
    private BasketRepository basketRepository;

    @MockBean
    private BasketMapper basketMapper;

    @MockBean
    private PhoneRepository phoneRepository;

    @Test
    void getByUserId() {
        User user = new User().toBuilder()
                .id(2L)
                .surname("Андрій")
                .name("Татарчук")
                .email("test2@gmail.com")
                .phone("380972553992")
                .build();

        Basket basket = new Basket().toBuilder()
                .id(1L)
                .user(user)
                .phones((List.of(new BasketPhone().toBuilder()
                        .id(new BasketPhonePK().toBuilder().basketId(1L).phoneId(4L).build())
                        .basket(new Basket().toBuilder().id(1L).build())
                        .phone(new Phone().toBuilder().id(4L).build())
                        .amount(1)
                        .build())))
                .price(300)
                .build();

        BasketDTO expected = new BasketDTO().toBuilder()
                .id(1L)
                .user(new UserDTO().toBuilder()
                        .id(2L)
                        .surname("Андрій")
                        .name("Татарчук")
                        .email("test2@gmail.com")
                        .phone("380972553992")
                        .build())
                .phones((List.of(new PhoneDTOWithOtherInfo().toBuilder()
                        .id(4L)
                        .brand("Apple")
                        .model("iPhone X")
                        .price(300)
                        .amount(10)
                        .color("Space grey")
                        .description("Nice phone")
                        .amountIn(1)
                        .build())))
                .price(300)
                .build();

        when(basketRepository.getByUserId(user.getId())).thenReturn(Optional.of(basket));
        when(basketMapper.toBasketDTO(basket)).thenReturn(expected);

        assertEquals(expected, basketService.getByUserId(user.getId()));

        verify(basketRepository, times(1)).getByUserId(user.getId());
        verify(basketMapper, times(1)).toBasketDTO(basket);
    }

    @Test
    void addToBasket() {
        BasketUpdateDTO dto = new BasketUpdateDTO().toBuilder()
                .userId(2L)
                .phoneId(4L)
                .isAdd(true)
                .amount(2)
                .build();

        Basket basket = new Basket().toBuilder()
                .id(1L)
                .user(new User().toBuilder()
                        .id(2L)
                        .surname("Андрій")
                        .name("Татарчук")
                        .email("test2@gmail.com")
                        .phone("380972553992")
                        .build())
                .phones(new ArrayList<>())
                .price(0)
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
                .id(new BasketPhonePK().toBuilder().basketId(basket.getId()).phoneId(phone.getId()).build())
                .basket(new Basket().toBuilder().id(basket.getId()).build())
                .phone(new Phone().toBuilder().id(phone.getId()).build())
                .amount(dto.getAmount())
                .build();

        BasketDTO expected = new BasketDTO().toBuilder()
                .id(basket.getId())
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
                        .amountIn(dto.getAmount())
                        .build())))
                .price(600)
                .build();

        when(basketRepository.getByUserId(dto.getUserId())).thenReturn(Optional.of(basket));
        when(phoneRepository.findById(dto.getPhoneId())).thenReturn(Optional.of(phone));
        when(basketPhoneRepository.save(basketPhone)).thenReturn(basketPhone);
        basket = basket.toBuilder()
                .phones(List.of(basketPhone))
                .price(600)
                .build();
        when(basketRepository.save(basket)).thenReturn(basket);
        when(basketMapper.toBasketDTO(basket)).thenReturn(expected);

        assertEquals(expected, basketService.update(dto));

        verify(basketRepository, times(1)).getByUserId(dto.getUserId());
        verify(phoneRepository, times(1)).findById(dto.getPhoneId());
        verify(basketPhoneRepository, times(1)).save(basketPhone);
        verify(basketRepository, times(1)).save(basket);
        verify(basketMapper, times(1)).toBasketDTO(basket);
    }

    @Test
    void deleteFromBasket() {
        BasketUpdateDTO dto = new BasketUpdateDTO().toBuilder()
                .userId(2L)
                .phoneId(4L)
                .isAdd(false)
                .amount(2)
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
                .phone(new Phone().toBuilder().id(phone.getId()).build())
                .amount(3)
                .build();

        Basket basket = new Basket().toBuilder()
                .id(1L)
                .user(new User().toBuilder()
                        .id(2L)
                        .surname("Андрій")
                        .name("Татарчук")
                        .email("test2@gmail.com")
                        .phone("380972553992")
                        .build())
                .phones(List.of(basketPhone))
                .price(basketPhone.getAmount() * phone.getPrice())
                .build();


        BasketDTO expected = new BasketDTO().toBuilder()
                .id(basket.getId())
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
                        .amountIn(1)
                        .build())))
                .price(300)
                .build();

        when(basketRepository.getByUserId(dto.getUserId())).thenReturn(Optional.of(basket));
        when(phoneRepository.findById(dto.getPhoneId())).thenReturn(Optional.of(phone));
        basketPhone.setAmount(basketPhone.getAmount() - dto.getAmount());
        basket.setPrice(basket.getPrice() - (dto.getAmount() * phone.getPrice()));
        when(basketRepository.save(basket)).thenReturn(basket);
        when(basketMapper.toBasketDTO(basket)).thenReturn(expected);

        assertEquals(expected, basketService.update(dto));

        verify(basketRepository, times(1)).getByUserId(dto.getUserId());
        verify(phoneRepository, times(1)).findById(dto.getPhoneId());
        verify(basketRepository, times(1)).save(basket);
        verify(basketMapper, times(1)).toBasketDTO(basket);
    }

    @Test
    void clear() {
        Basket basket = new Basket().toBuilder()
                .id(1L)
                .user(new User().toBuilder()
                        .id(2L)
                        .surname("Андрій")
                        .name("Татарчук")
                        .email("test2@gmail.com")
                        .phone("380972553992")
                        .build())
                .phones(List.of(new BasketPhone().toBuilder()
                        .id(new BasketPhonePK().toBuilder().basketId(1L).phoneId(4L).build())
                        .basket(new Basket().toBuilder().id(1L).build())
                        .phone(new Phone().toBuilder().id(4L).build())
                        .amount(3)
                        .build()))
                .price(900)
                .build();

        when(basketRepository.getByUserId(basket.getUser().getId())).thenReturn(Optional.of(basket));
        basket.setPhones(new ArrayList<>());
        basket.setPrice(0);
        when(basketRepository.save(basket)).thenReturn(basket);

        assertTrue(basketService.clear(basket.getUser().getId()));

        verify(basketRepository, times(1)).getByUserId(basket.getUser().getId());
        verify(basketPhoneRepository, times(1)).deleteAll(basket.getPhones());
        verify(basketRepository, times(1)).save(basket);
    }
}