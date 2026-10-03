package com.pvz.demo2.service;

import com.pvz.demo2.exception.ResourceNotFoundException;
import com.pvz.demo2.model.Parcel;
import com.pvz.demo2.model.ParcelStatus;
import com.pvz.demo2.repository.ParcelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParcelService {

    private final ParcelRepository parcelRepository;

    @Transactional(readOnly = true)
    public List<Parcel> findAll() {
        return parcelRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Parcel findById(Long id) {
        return parcelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Посылка не найдена: id=" + id));
    }

    @Transactional(readOnly = true)
    public List<Parcel> search(String trackingPart) {
        if (trackingPart == null || trackingPart.isBlank()) {
            return parcelRepository.findAll();
        }
        return parcelRepository.findByTrackingNumberContainingIgnoreCase(trackingPart);
    }

    @Transactional
    public Parcel save(Parcel parcel) {
        return parcelRepository.save(parcel);
    }

    @Transactional
    public void delete(Long id) {
        parcelRepository.deleteById(id);
    }

    /** Смена статуса админом. */
    @Transactional
    public Parcel changeStatus(Long id, ParcelStatus newStatus) {
        Parcel parcel = findById(id);
        parcel.setStatus(newStatus);
        return parcelRepository.save(parcel);
    }
}