package com.pvz.demo2.model;

import com.pvz.demo2.model.ParcelStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "parcels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String trackingNumber;

    @Column
    private String description;

    @Column
    private Double weight;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Column(name = "storage_until")
    private LocalDate storageUntil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParcelStatus status = ParcelStatus.NEW;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private PvzService service;

    @OneToOne(mappedBy = "parcel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Order order;
}