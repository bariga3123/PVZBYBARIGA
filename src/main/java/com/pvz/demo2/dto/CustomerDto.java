package com.pvz.demo2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {

    @NotBlank(message = "ФИО обязательно")
    @Size(max = 150)
    private String fullName;

    @Size(max = 250)
    private String address;

    @Pattern(regexp = "^$|^\\+?[0-9\\-\\s]{10,20}$",
            message = "Телефон в формате +7-900-000-00-00")
    private String phone;

    /** ID владельца (пользователя). */
    private Long userId;
}