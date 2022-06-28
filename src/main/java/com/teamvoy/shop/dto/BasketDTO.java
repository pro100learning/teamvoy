package com.teamvoy.shop.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BasketDTO {

    private Long id;

    private UserDTO user;

    private List<PhoneDTOWithOtherInfo> phones;

    private double price;
}
