package com.pvz.demo2.service;

import com.pvz.demo2.exception.ResourceNotFoundException;
import com.pvz.demo2.model.Cell;
import com.pvz.demo2.repository.CellRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CellService {

    private final CellRepository cellRepository;

    @Transactional(readOnly = true)
    public List<Cell> findAll() {
        return cellRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Cell> findFree() {
        return cellRepository.findByIsOccupiedFalse();
    }

    @Transactional(readOnly = true)
    public Cell findById(Long id) {
        return cellRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ячейка не найдена: id=" + id));
    }

    @Transactional
    public Cell save(Cell cell) {
        return cellRepository.save(cell);
    }

    @Transactional
    public void delete(Long id) {
        cellRepository.deleteById(id);
    }
}