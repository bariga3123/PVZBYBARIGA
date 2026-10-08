package com.pvz.demo2.controller;

import com.pvz.demo2.model.PvzService;
import com.pvz.demo2.service.PvzServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PvzServiceService pvzServiceService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/contacts")
    public String contacts() {
        return "home/contacts";
    }

    @GetMapping("/faq")
    public String faq() {
        return "home/faq";
    }

    // ===== Каталог услуг =====

    @GetMapping("/catalog")
    public String catalog(Model model) {
        List<PvzService> services = pvzServiceService.findAll();
        model.addAttribute("services", services);
        return "catalog/list";
    }

    @GetMapping("/catalog/{id}")
    public String catalogCard(@PathVariable Long id, Model model) {
        PvzService service = pvzServiceService.findById(id);
        model.addAttribute("service", service);
        return "catalog/card";
    }
}