package com.teamvoy.shop.service.impl;

import com.teamvoy.shop.dto.PhoneDTO;
import com.teamvoy.shop.entity.Phone;
import com.teamvoy.shop.mapper.PhoneMapper;
import com.teamvoy.shop.repository.PhoneRepository;
import com.teamvoy.shop.service.PhoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper;

    @Override
    public PhoneDTO create(PhoneDTO dto) {
        Phone phone = phoneMapper.toEntity(dto);
        return phoneMapper.toPhoneDTO(
                phoneRepository.save(phone)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PhoneDTO getById(Long phoneId) {
        return phoneMapper.toPhoneDTO(
                phoneRepository.findById(phoneId).orElseThrow()
        );
    }

    @Override
    public PhoneDTO update(PhoneDTO dto) {
        Phone phone = phoneMapper.toEntity(dto);
        return phoneMapper.toPhoneDTO(
                phoneRepository.save(phone)
        );
    }

    @Override
    public boolean delete(Long phoneId) {
        phoneRepository.deleteById(phoneId);
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhoneDTO> getAll() {
        return phoneMapper.toPhoneDTO(
                phoneRepository.findAll()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public long getCount() {
        return phoneRepository.getCount();
    }
}
