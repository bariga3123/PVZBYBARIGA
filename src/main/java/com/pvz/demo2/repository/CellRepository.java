package com.pvz.demo2.repository;

import com.pvz.demo2.model.Cell;
import com.pvz.demo2.model.CellSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CellRepository extends JpaRepository<Cell, Long> {

    List<Cell> findByIsOccupiedFalse();

    List<Cell> findBySize(CellSize size);

    Optional<Cell> findByCellNumber(String cellNumber);
}