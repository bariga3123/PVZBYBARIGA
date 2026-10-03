package com.pvz.demo2.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelDto {

    @NotBlank(message = "Трек-номер обязателен")
    @Size(max = 50)
    private String trackingNumber;

    @Size(max = 500, message = "Описание до 500 символов")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Вес должен быть больше 0")
    private Double weight;

    @NotNull(message = "Дата поступления обязательна")
    private LocalDate arrivalDate;

    private LocalDate storageUntil;

    /** ID получателя — выбирается из списка. */
    @NotNull(message = "Получатель обязателен")
    private Long customerId;

    /** ID услуги — необязателен. */
    private Long serviceId;
}