package com.teamvoy.shop.mapper;

import com.teamvoy.shop.dto.BasketDTO;
import com.teamvoy.shop.dto.PhoneDTOWithOtherInfo;
import com.teamvoy.shop.entity.Basket;
import com.teamvoy.shop.entity.BasketPhone;
import com.teamvoy.shop.entity.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = {UserMapper.class, PhoneMapper.class})
public interface BasketMapper {

    @Named("toPhones")
    default List<PhoneDTOWithOtherInfo> toPhones(List<BasketPhone> phones) {
        if (phones == null) {
            return new ArrayList<>();
        }
        return phones.stream()
                .map(basketPhone -> {
                    Phone phone = basketPhone.getPhone();
                    return new PhoneDTOWithOtherInfo().toBuilder()
                            .id(phone.getId())
                            .brand(phone.getBrand())
                            .model(phone.getModel())
                            .price(phone.getPrice())
                            .amount(phone.getAmount())
                            .color(phone.getColor())
                            .description(phone.getDescription())
                            .amountInBasket(basketPhone.getAmount())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Mapping(source = "phones", target = "phones", qualifiedByName = "toPhones")
    BasketDTO toBasketDTO(Basket basket);

    @Mapping(source = "phones", target = "phones", qualifiedByName = "toPhones")
    List<BasketDTO> toBasketDTO(List<Basket> baskets);
}
