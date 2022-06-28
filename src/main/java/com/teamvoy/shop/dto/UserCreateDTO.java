package com.teamvoy.shop.dto;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserCreateDTO {

    @NotNull
    @NotBlank(message = "Name cannot be empty")
    @Size(max = 255, message = "Name must be to up 255 characters")
    private String name;

    @NotNull
    @NotBlank(message = "Surname cannot be empty")
    @Size(max = 255, message = "Surname must be to up 255 characters")
    private String surname;

    @NotNull
    @Email(message = "Email does not match format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotNull
    @NotBlank(message = "Phone cannot be empty")
    @Size(min = 10, max = 14, message = "Phone must be between 10 and 14 characters")
    @Pattern(regexp = "[0-9]{10,15}",
            message = "The phone can contain numbers, and punctuation characters. Phone must be between 10 and 14 characters")
    private String phone;

    @NotNull
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,64}+$",
            message = "The password can contain lowercase and uppercase Latin letters, numbers, and special characters")
    private String password;
}
