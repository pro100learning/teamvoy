package com.teamvoy.shop.service.impl;

import com.teamvoy.shop.dto.BasketDTO;
import com.teamvoy.shop.dto.BasketUpdateDTO;
import com.teamvoy.shop.entity.Basket;
import com.teamvoy.shop.entity.BasketPhone;
import com.teamvoy.shop.entity.BasketPhonePK;
import com.teamvoy.shop.entity.Phone;
import com.teamvoy.shop.mapper.BasketMapper;
import com.teamvoy.shop.repository.BasketPhoneRepository;
import com.teamvoy.shop.repository.BasketRepository;
import com.teamvoy.shop.repository.PhoneRepository;
import com.teamvoy.shop.repository.UserRepository;
import com.teamvoy.shop.service.BasketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BasketServiceImpl implements BasketService {


    private final BasketPhoneRepository basketPhoneRepository;
    private final BasketRepository basketRepository;
    private final BasketMapper basketMapper;

    private final UserRepository userRepository;

    private final PhoneRepository phoneRepository;

    @Override
    public BasketDTO getByUserId(Long userId) {
        Basket basket = basketRepository.getByUserId(userId).orElse(null);
        if (basket == null) {
            basket = new Basket().toBuilder()
                    .user(userRepository.findById(userId).orElseThrow())
                    .build();
            basketRepository.save(basket);
        }
        return basketMapper.toBasketDTO(
                basket
        );
    }

    @Override
    public BasketDTO update(BasketUpdateDTO dto) {
        Basket basket = basketRepository.getByUserId(dto.getUserId()).orElse(null);
        if (basket == null) {
            basket = new Basket().toBuilder()
                    .user(userRepository.findById(dto.getUserId()).orElseThrow())
                    .build();
        }
        Phone phone = phoneRepository.findById(dto.getPhoneId()).orElseThrow();
        if(dto.getAmount() > phone.getAmount()) {
            throw new IllegalArgumentException("There are no such goods in such quantity");
        }
        if (dto.isAdd()) {
            if (basket.getPhones().stream().anyMatch(basketPhone -> basketPhone.getPhone().equals(phone))) {
                BasketPhone basketPhone = basket.getPhones().stream()
                        .filter(b -> b.getPhone().getId().equals(phone.getId()))
                        .findFirst().orElseThrow();

                if(basketPhone.getAmount() + dto.getAmount() > phone.getAmount()) {
                    throw new IllegalArgumentException("The store does not have such a quantity of goods");
                }

                basketPhone.setAmount(basketPhone.getAmount() + dto.getAmount());
                basket.setPrice(basket.getPrice() + (dto.getAmount() * phone.getPrice()));
            } else {
                BasketPhone basketPhone = new BasketPhone().toBuilder()
                        .id(new BasketPhonePK().toBuilder().basketId(basket.getId()).phoneId(phone.getId()).build())
                        .basket(new Basket().toBuilder().id(basket.getId()).build())
                        .phone(new Phone().toBuilder().id(phone.getId()).build())
                        .amount(dto.getAmount())
                        .build();
                basketPhone = basketPhoneRepository.save(basketPhone);
                basket.getPhones().add(basketPhone);
                basket.setPrice(basket.getPrice() + (dto.getAmount() * phone.getPrice()));
            }
        } else {
            BasketPhone basketPhone = basket.getPhones().stream()
                    .filter(b -> b.getPhone().getId().equals(phone.getId()))
                    .findFirst().orElseThrow(() -> {
                        throw new NoSuchElementException("This product is not in the basket");
                    });
            if(dto.getAmount() > basketPhone.getAmount()) {
                basketPhone.setAmount(0);
                basket.setPrice(basket.getPrice() - (basketPhone.getAmount() * phone.getPrice()));
            }
            else {
                basketPhone.setAmount(basketPhone.getAmount() - dto.getAmount());
                basket.setPrice(basket.getPrice() - (dto.getAmount() * phone.getPrice()));
            }
            if (basketPhone.getAmount() == 0) {
                basketPhoneRepository.delete(basketPhone);
            }
        }
        return basketMapper.toBasketDTO(
                basketRepository.save(basket)
        );
    }

    @Override
    public boolean clear(Long userId) {
        Basket basket = basketRepository.getByUserId(userId).orElseThrow();
        basketPhoneRepository.deleteAll(basket.getPhones());
        basket.setPhones(new ArrayList<>());
        basket.setPrice(0);
        basketRepository.save(basket);
        return true;
    }
}
