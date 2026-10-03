package com.pvz.demo2.repository;

import com.pvz.demo2.model.Order;
import com.pvz.demo2.model.ParcelStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByParcelId(Long parcelId);

    List<Order> findByStatus(ParcelStatus status);

    List<Order> findByParcelCustomerUserId(Long userId);
}