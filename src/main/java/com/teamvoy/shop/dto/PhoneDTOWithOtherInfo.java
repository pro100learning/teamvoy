package com.teamvoy.shop.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PhoneDTOWithOtherInfo {

    private Long id;

    private String brand;

    private String model;

    private double price;

    private int amount;

    private String color;

    private String description;

    private int amountInBasket;
}
