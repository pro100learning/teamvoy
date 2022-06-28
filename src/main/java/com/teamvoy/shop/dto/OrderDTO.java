package com.teamvoy.shop.dto;

import com.teamvoy.shop.entity.enums.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderDTO {

    private Long id;

    private UserDTO user;

    private List<PhoneDTOWithOtherInfo> phones;

    private Status status;

    private double price;

    private boolean paid;

    private LocalDateTime createdAt;
}
