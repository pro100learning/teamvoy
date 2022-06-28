package com.teamvoy.shop.dto;

import com.teamvoy.shop.entity.enums.Status;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderUpdateDTO {

    @NotNull
    @Min(value = 0, message = "Order id cannot be less than 0")
    private Long id;

    @NotNull
    private Status status;
}
