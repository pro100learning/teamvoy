package com.teamvoy.shop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PhoneDTO {

    private Long id;

    @NotNull
    @NotBlank(message = "Brand cannot be empty")
    @Size(max = 255, message = "Brand must be to up 255 characters")
    private String brand;

    @NotNull
    @NotBlank(message = "Model cannot be empty")
    @Size(max = 255, message = "Model must be to up 255 characters")
    private String model;

    @NotNull
    @Min(value = 0, message = "Price cannot be less than 0")
    private double price;

    @NotNull
    @Min(value = 0, message = "Amount cannot be less than 0")
    private int amount;

    private String color;

    private String description;
}
