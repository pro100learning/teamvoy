package com.teamvoy.shop.service;

import com.teamvoy.shop.dto.BasketDTO;
import com.teamvoy.shop.dto.BasketUpdateDTO;

public interface BasketService {

    BasketDTO getByUserId(Long userId);
    BasketDTO update(BasketUpdateDTO dto);
    boolean clear(Long userId);
}
