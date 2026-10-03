package com.pvz.demo2.repository;

import com.pvz.demo2.model.PvzService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PvzServiceRepository extends JpaRepository<PvzService, Long> {

    List<PvzService> findByCategory(String category);

    List<PvzService> findByNameContainingIgnoreCase(String name);
}