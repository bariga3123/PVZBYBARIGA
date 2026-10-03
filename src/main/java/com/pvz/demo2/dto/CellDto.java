package com.pvz.demo2.dto;

import com.pvz.demo2.model.CellSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CellDto {

    @NotBlank(message = "Номер ячейки обязателен")
    @Size(max = 20)
    private String cellNumber;

    @NotNull(message = "Размер обязателен")
    private CellSize size;

    private Boolean isOccupied;
}