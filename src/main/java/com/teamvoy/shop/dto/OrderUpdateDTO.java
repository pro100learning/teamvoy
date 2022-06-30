package com.teamvoy.shop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Min(value = 1, message = "Order id cannot be less than 1")
    private Long id;

    @JsonIgnore
    private Long userId;

    @NotNull
    private Status status;
}
