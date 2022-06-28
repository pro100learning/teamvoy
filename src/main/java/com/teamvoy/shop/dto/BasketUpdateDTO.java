package com.teamvoy.shop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BasketUpdateDTO {

    @JsonIgnore
    private Long userId;

    @NotNull
    @Min(value = 0, message = "Phone id cannot be less than 0")
    private Long phoneId;

    @JsonProperty("isAdd")
    @NotNull(message = "isAdd cannot be null")
    private boolean isAdd;

    @NotNull
    @Min(value = 1, message = "Amount cannot be less than 1")
    private int amount;
}
