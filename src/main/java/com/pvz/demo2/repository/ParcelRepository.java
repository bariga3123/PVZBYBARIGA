package com.pvz.demo2.repository;

import com.pvz.demo2.model.Parcel;
import com.pvz.demo2.model.ParcelStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {

    List<Parcel> findByCustomerId(Long customerId);

    Optional<Parcel> findByTrackingNumber(String trackingNumber);

    List<Parcel> findByTrackingNumberContainingIgnoreCase(String part);

    List<Parcel> findByStatus(ParcelStatus status);
}