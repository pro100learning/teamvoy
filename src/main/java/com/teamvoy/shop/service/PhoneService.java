package com.teamvoy.shop.service;

import com.teamvoy.shop.dto.PhoneDTO;

import java.util.List;

public interface PhoneService {

    PhoneDTO create(PhoneDTO dto);

    PhoneDTO getById(Long phoneId);

    PhoneDTO update(PhoneDTO dto);

    boolean delete(Long phoneId);

    List<PhoneDTO> getAll();

    long getCount();
}
