package com.pvz.demo2.repository;

import com.pvz.demo2.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByUserId(Long userId);
    List<Customer> findByFullNameContainingIgnoreCase(String fullName);
}