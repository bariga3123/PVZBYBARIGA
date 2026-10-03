package com.pvz.demo2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cells")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "cell_number", nullable = false, unique = true)
    private String cellNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CellSize size;

    @Column(name = "is_occupied", nullable = false)
    private Boolean isOccupied = false;

    @OneToMany(mappedBy = "cell")
    private List<Order> orders = new ArrayList<>();
}