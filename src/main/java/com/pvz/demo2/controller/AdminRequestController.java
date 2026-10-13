package com.pvz.demo2.controller;

import com.pvz.demo2.model.Order;
import com.pvz.demo2.model.ParcelStatus;
import com.pvz.demo2.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/requests")
@RequiredArgsConstructor
public class AdminRequestController {

    private final OrderService orderService;

    @GetMapping
    public String list(@RequestParam(value = "status", required = false) ParcelStatus status,
                       Model model) {
        List<Order> orders = (status == null)
                ? orderService.findAll()
                : orderService.findAll().stream().filter(o -> o.getStatus() == status).toList();

        model.addAttribute("orders", orders);
        model.addAttribute("statuses", ParcelStatus.values());
        model.addAttribute("selectedStatus", status);
        return "admin/requests";
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id,
                               @RequestParam("status") ParcelStatus status,
                               @RequestParam(value = "comment", required = false) String comment,
                               RedirectAttributes ra) {
        try {
            orderService.changeStatus(id, status, comment);
            ra.addFlashAttribute("success", "Статус изменён на " + status);
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/requests";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            orderService.delete(id);
            ra.addFlashAttribute("success", "Заявка #" + id + " удалена");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/requests";
    }
}