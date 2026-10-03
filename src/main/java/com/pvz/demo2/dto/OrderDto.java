package com.pvz.demo2.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    @NotNull(message = "Посылка обязательна")
    private Long parcelId;

    /** ID ячейки — необязателен, админ может назначить позже. */
    private Long cellId;

    @Size(max = 500, message = "Комментарий до 500 символов")
    private String comment;
}