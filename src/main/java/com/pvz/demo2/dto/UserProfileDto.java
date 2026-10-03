package com.pvz.demo2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {

    @NotBlank(message = "Логин обязателен")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    private String email;

    @Pattern(regexp = "^$|^\\+?[0-9\\-\\s]{10,20}$",
            message = "Телефон в формате +7-900-000-00-00")
    private String phone;
}