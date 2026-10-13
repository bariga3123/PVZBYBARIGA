package com.pvz.demo2.service;

import com.pvz.demo2.exception.ResourceNotFoundException;
import com.pvz.demo2.model.*;
import com.pvz.demo2.repository.CellRepository;
import com.pvz.demo2.repository.OrderRepository;
import com.pvz.demo2.repository.ParcelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ParcelRepository parcelRepository;
    private final CellRepository cellRepository;

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Заявка не найдена: id=" + id));
    }

    @Transactional(readOnly = true)
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByParcelCustomerUserId(userId);
    }

    /** Создание заявки пользователем. */
    @Transactional
    public Order create(Long parcelId, Long cellId, String comment, String username) {
        Parcel parcel = parcelRepository.findById(parcelId)
                .orElseThrow(() -> new ResourceNotFoundException("Посылка не найдена"));

        // Проверяем, что посылка принадлежит этому пользователю
        if (!parcel.getCustomer().getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Это не ваша посылка");
        }

        // Проверяем, что заявки ещё нет
        if (orderRepository.findByParcelId(parcelId).isPresent()) {
            throw new IllegalArgumentException("Заявка по этой посылке уже существует");
        }

        Order order = Order.builder()
                .parcel(parcel)
                .status(ParcelStatus.NEW)
                .comment(comment)
                .build();

        if (cellId != null) {
            Cell cell = cellRepository.findById(cellId)
                    .orElseThrow(() -> new ResourceNotFoundException("Ячейка не найдена"));
            if (Boolean.TRUE.equals(cell.getIsOccupied())) {
                throw new IllegalArgumentException("Ячейка уже занята");
            }
            cell.setIsOccupied(true);
            cellRepository.save(cell);
            order.setCell(cell);
        }

        return orderRepository.save(order);
    }

    /** Отмена заявки пользователем. Только если статус NEW. */
    @Transactional
    public void cancel(Long orderId, String username) {
        Order order = findById(orderId);

        if (!order.getParcel().getCustomer().getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Это не ваша заявка");
        }
        if (order.getStatus() != ParcelStatus.NEW) {
            throw new IllegalStateException("Нельзя отменить: админ уже взял в работу");
        }

        // Освобождаем ячейку, если была
        if (order.getCell() != null) {
            Cell cell = order.getCell();
            cell.setIsOccupied(false);
            cellRepository.save(cell);
        }

        orderRepository.delete(order);
    }

    /** Смена статуса админом. */
    @Transactional
    public Order changeStatus(Long orderId, ParcelStatus newStatus) {
        Order order = findById(orderId);
        order.setStatus(newStatus);

        // Если COMPLETED — ставим дату выдачи и освобождаем ячейку
        if (newStatus == ParcelStatus.COMPLETED) {
            order.setIssueDate(LocalDate.now());
            if (order.getCell() != null) {
                Cell cell = order.getCell();
                cell.setIsOccupied(false);
                cellRepository.save(cell);
            }
        }
        return orderRepository.save(order);
    }

    /** Удаление заявки админом. Освобождает ячейку, если была занята. */
    @Transactional
    public void delete(Long id) {
        Order order = findById(id);

        if (order.getCell() != null) {
            Cell cell = order.getCell();
            cell.setIsOccupied(false);
            cellRepository.save(cell);
        }

        orderRepository.delete(order);
    }
    @Transactional
    public Order changeStatus(Long orderId, ParcelStatus newStatus, String comment) {
        Order order = findById(orderId);
        order.setStatus(newStatus);

        if (comment != null && !comment.isBlank()) {
            order.setComment(comment);
        }

        if (newStatus == ParcelStatus.COMPLETED) {
            order.setIssueDate(LocalDate.now());
            if (order.getCell() != null) {
                order.getCell().setIsOccupied(false);
                cellRepository.save(order.getCell());
            }
        }
        return orderRepository.save(order);
    }
}