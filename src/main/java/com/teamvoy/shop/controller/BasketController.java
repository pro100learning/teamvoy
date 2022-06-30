package com.teamvoy.shop.controller;

import com.teamvoy.shop.annotation.CurrentUser;
import com.teamvoy.shop.dto.BasketDTO;
import com.teamvoy.shop.dto.BasketUpdateDTO;
import com.teamvoy.shop.security.UserSecurity;
import com.teamvoy.shop.service.BasketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("basket")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BasketController {

    private final BasketService basketService;

    @GetMapping
    public BasketDTO getBasket(
            @CurrentUser UserSecurity userSecurity
    ) {
        return basketService.getByUserId(userSecurity.getId());
    }

    @PutMapping
    public BasketDTO update(
            @Valid @RequestBody BasketUpdateDTO dto,
            @CurrentUser UserSecurity userSecurity
    ) {
        dto.setUserId(userSecurity.getId());
        return basketService.update(dto);
    }

    @DeleteMapping
    public boolean clear(
            @CurrentUser UserSecurity userSecurity
    ) {
        return basketService.clear(userSecurity.getId());
    }
}
