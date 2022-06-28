package com.teamvoy.shop.mapper;

import com.teamvoy.shop.dto.PhoneDTO;
import com.teamvoy.shop.entity.Phone;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface PhoneMapper {

    Phone toEntity(PhoneDTO dto);


    PhoneDTO toPhoneDTO(Phone phone);

    List<PhoneDTO> toPhoneDTO(List<Phone> phones);
}
