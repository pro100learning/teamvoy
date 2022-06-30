package com.teamvoy.shop.mapper;

import com.teamvoy.shop.dto.PhoneDTOWithOtherInfo;
import com.teamvoy.shop.dto.OrderDTO;
import com.teamvoy.shop.entity.Order;
import com.teamvoy.shop.entity.OrderPhone;
import com.teamvoy.shop.entity.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = {UserMapper.class, PhoneMapper.class})
public interface OrderMapper {

    @Named("toPhones")
    default List<PhoneDTOWithOtherInfo> toPhones(List<OrderPhone> phones) {
        if (phones == null) {
            return new ArrayList<>();
        }
        return phones.stream()
                .map(orderPhone -> {
                    Phone phone = orderPhone.getPhone();
                    return new PhoneDTOWithOtherInfo().toBuilder()
                            .id(phone.getId())
                            .brand(phone.getBrand())
                            .model(phone.getModel())
                            .price(phone.getPrice())
                            .amount(phone.getAmount())
                            .color(phone.getColor())
                            .description(phone.getDescription())
                            .amountIn(orderPhone.getAmount())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Mapping(source = "phones", target = "phones", qualifiedByName = "toPhones")
    OrderDTO toOrderDTO(Order order);

    @Mapping(source = "phones", target = "phones", qualifiedByName = "toPhones")
    List<OrderDTO> toOrderDTO(List<Order> orders);
}
