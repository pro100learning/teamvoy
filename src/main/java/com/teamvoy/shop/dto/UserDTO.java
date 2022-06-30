package com.teamvoy.shop.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDTO {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private String phone;
}
