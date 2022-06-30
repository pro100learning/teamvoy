package com.teamvoy.shop.service;

import com.teamvoy.shop.dto.PhoneDTO;
import com.teamvoy.shop.entity.Phone;
import com.teamvoy.shop.mapper.PhoneMapper;
import com.teamvoy.shop.repository.PhoneRepository;
import com.teamvoy.shop.service.PhoneService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class PhoneServiceTest {

    @Autowired
    private PhoneService phoneService;

    @MockBean
    private PhoneRepository phoneRepository;

    @MockBean
    private PhoneMapper phoneMapper;

    @Test
    void create() {
        PhoneDTO expected = new PhoneDTO().toBuilder()
                .brand("Apple")
                .model("iPhone XR")
                .price(550)
                .amount(50)
                .color("Yellow")
                .description("Phone with one camera")
                .build();

        Phone phone = new Phone().toBuilder()
                .brand("Apple")
                .model("iPhone XR")
                .price(550)
                .amount(50)
                .color("Yellow")
                .description("Phone with one camera")
                .build();

        when(phoneMapper.toEntity(expected)).thenReturn(phone);
        when(phoneRepository.save(phone)).thenReturn(phone);
        when(phoneMapper.toPhoneDTO(phone)).thenReturn(expected);

        assertEquals(expected, phoneService.create(expected));

        verify(phoneMapper, times(1)).toEntity(expected);
        verify(phoneRepository, times(1)).save(phone);
        verify(phoneMapper, times(1)).toPhoneDTO(phone);
    }

    @Test
    void getById() {
        Phone phone = new Phone().toBuilder()
                .id(6L)
                .brand("Apple")
                .model("iPhone XR")
                .price(550)
                .amount(50)
                .color("Yellow")
                .description("Phone with one camera")
                .build();

        PhoneDTO expected = new PhoneDTO().toBuilder()
                .id(6L)
                .brand("Apple")
                .model("iPhone XR")
                .price(550)
                .amount(50)
                .color("Yellow")
                .description("Phone with one camera")
                .build();

        when(phoneRepository.findById(phone.getId())).thenReturn(Optional.of(phone));
        when(phoneMapper.toPhoneDTO(phone)).thenReturn(expected);

        assertEquals(expected, phoneService.getById(phone.getId()));

        verify(phoneRepository, times(1)).findById(phone.getId());
        verify(phoneMapper, times(1)).toPhoneDTO(phone);
    }

    @Test
    void update() {
        PhoneDTO expected = new PhoneDTO().toBuilder()
                .id(6L)
                .brand("Samsung")
                .model("A51")
                .price(250)
                .amount(25)
                .color("Red")
                .description("Nice phone")
                .build();

        Phone newPhone = new Phone().toBuilder()
                .id(6L)
                .brand("Samsung")
                .model("A51")
                .price(250)
                .amount(25)
                .color("Red")
                .description("Nice phone")
                .build();

        when(phoneMapper.toEntity(expected)).thenReturn(newPhone);
        when(phoneRepository.save(newPhone)).thenReturn(newPhone);
        when(phoneMapper.toPhoneDTO(newPhone)).thenReturn(expected);

        assertEquals(expected, phoneService.create(expected));

        verify(phoneMapper, times(1)).toEntity(expected);
        verify(phoneRepository, times(1)).save(newPhone);
        verify(phoneMapper, times(1)).toPhoneDTO(newPhone);
    }

    @Test
    void delete() {
        Phone phone = new Phone().toBuilder()
                .id(6L)
                .brand("Apple")
                .model("iPhone XR")
                .price(550)
                .amount(50)
                .color("Yellow")
                .description("Phone with one camera")
                .build();

        assertTrue(phoneService.delete(phone.getId()));

        verify(phoneRepository, times(1)).deleteById(phone.getId());
    }

    @Test
    void getAll() {
        List<Phone> phones = new ArrayList<>();

        phones.add(new Phone().toBuilder()
                .id(1L)
                .brand("Apple")
                .model("iPhone 11")
                .price(600)
                .amount(23)
                .color("Black")
                .description("Nice phone")
                .build());

        phones.add(new Phone().toBuilder()
                .id(2L)
                .brand("Apple")
                .model("iPhone 11")
                .price(600)
                .amount(52)
                .color("Red")
                .description("Nice phone")
                .build());

        phones.add(new Phone().toBuilder()
                .id(3L)
                .brand("Apple")
                .model("iPhone 11")
                .price(600)
                .amount(35)
                .color("White")
                .description("Nice phone")
                .build());

        phones.add(new Phone().toBuilder()
                .id(4L)
                .brand("Apple")
                .model("iPhone X")
                .price(300)
                .amount(10)
                .color("Space grey")
                .description("Nice phone")
                .build());

        phones.add(new Phone().toBuilder()
                .id(5L)
                .brand("Apple")
                .model("iPhone 13 Pro Max")
                .price(1200)
                .amount(40)
                .color("White")
                .description("Nice phone")
                .build());

        List<PhoneDTO> phonesDTO = new ArrayList<>();

        phonesDTO.add(new PhoneDTO().toBuilder()
                .id(1L)
                .brand("Apple")
                .model("iPhone 11")
                .price(600)
                .amount(23)
                .color("Black")
                .description("Nice phone")
                .build());

        phonesDTO.add(new PhoneDTO().toBuilder()
                .id(2L)
                .brand("Apple")
                .model("iPhone 11")
                .price(600)
                .amount(52)
                .color("Red")
                .description("Nice phone")
                .build());

        phonesDTO.add(new PhoneDTO().toBuilder()
                .id(3L)
                .brand("Apple")
                .model("iPhone 11")
                .price(600)
                .amount(35)
                .color("White")
                .description("Nice phone")
                .build());

        phonesDTO.add(new PhoneDTO().toBuilder()
                .id(4L)
                .brand("Apple")
                .model("iPhone X")
                .price(300)
                .amount(10)
                .color("Space grey")
                .description("Nice phone")
                .build());

        phonesDTO.add(new PhoneDTO().toBuilder()
                .id(5L)
                .brand("Apple")
                .model("iPhone 13 Pro Max")
                .price(1200)
                .amount(40)
                .color("White")
                .description("Nice phone")
                .build());

        when(phoneRepository.findAll()).thenReturn(phones);
        when(phoneMapper.toPhoneDTO(phones)).thenReturn(phonesDTO);

        assertEquals(phonesDTO, phoneService.getAll());

        verify(phoneRepository, times(1)).findAll();
        verify(phoneMapper, times(1)).toPhoneDTO(phones);
    }

    @Test
    void getCount() {
        List<Phone> phones = new ArrayList<>();

        phones.add(new Phone().toBuilder()
                .id(1L)
                .brand("Apple")
                .model("iPhone 11")
                .price(600)
                .amount(23)
                .color("Black")
                .description("Nice phone")
                .build());

        phones.add(new Phone().toBuilder()
                .id(2L)
                .brand("Apple")
                .model("iPhone 11")
                .price(600)
                .amount(52)
                .color("Red")
                .description("Nice phone")
                .build());

        phones.add(new Phone().toBuilder()
                .id(3L)
                .brand("Apple")
                .model("iPhone 11")
                .price(600)
                .amount(35)
                .color("White")
                .description("Nice phone")
                .build());

        phones.add(new Phone().toBuilder()
                .id(4L)
                .brand("Apple")
                .model("iPhone X")
                .price(300)
                .amount(10)
                .color("Space grey")
                .description("Nice phone")
                .build());

        phones.add(new Phone().toBuilder()
                .id(5L)
                .brand("Apple")
                .model("iPhone 13 Pro Max")
                .price(1200)
                .amount(40)
                .color("White")
                .description("Nice phone")
                .build());

        when(phoneRepository.getCount()).thenReturn(5L);

        assertEquals(phones.size(), phoneService.getCount());

        verify(phoneRepository, times(1)).getCount();
    }
}