package com.pvz.demo2.controller.admin;

import com.pvz.demo2.model.ParcelStatus;
import com.pvz.demo2.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final OrderRepository orderRepository;
    private final ParcelRepository parcelRepository;
    private final CellRepository cellRepository;
    private final PvzServiceRepository serviceRepository;
    private final UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalOrders",    orderRepository.count());
        model.addAttribute("newOrders",      orderRepository.findByStatus(ParcelStatus.NEW).size());
        model.addAttribute("inProgress",     orderRepository.findByStatus(ParcelStatus.IN_PROGRESS).size());
        model.addAttribute("completed",      orderRepository.findByStatus(ParcelStatus.COMPLETED).size());
        model.addAttribute("totalParcels",   parcelRepository.count());
        model.addAttribute("totalCells",     cellRepository.count());
        model.addAttribute("freeCells",      cellRepository.findByIsOccupiedFalse().size());
        model.addAttribute("totalServices",  serviceRepository.count());
        model.addAttribute("totalUsers",     userRepository.count());
        return "admin/dashboard";
    }
}