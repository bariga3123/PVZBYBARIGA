package com.pvz.demo2.service;

import com.pvz.demo2.exception.ResourceNotFoundException;
import com.pvz.demo2.model.PvzService;
import com.pvz.demo2.repository.PvzServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PvzServiceService {

    private final PvzServiceRepository pvzServiceRepository;

    @Transactional(readOnly = true)
    public List<PvzService> findAll() {
        return pvzServiceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PvzService findById(Long id) {
        return pvzServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Услуга не найдена: id=" + id));
    }

    @Transactional
    public PvzService save(PvzService service) {
        return pvzServiceRepository.save(service);
    }

    @Transactional
    public void delete(Long id) {
        pvzServiceRepository.deleteById(id);
    }
}