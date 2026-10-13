package com.pvz.demo2.controller.admin;

import com.pvz.demo2.dto.PvzServiceDto;
import com.pvz.demo2.model.PvzService;
import com.pvz.demo2.service.PvzServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/services")
@RequiredArgsConstructor
public class AdminServiceController {

    private final PvzServiceService service;

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q, Model model) {
        model.addAttribute("items", service.findAll());
        model.addAttribute("q", q);
        return "admin/services/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("dto", new PvzServiceDto());
        model.addAttribute("id", null);
        return "admin/services/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        PvzService s = service.findById(id);
        PvzServiceDto dto = PvzServiceDto.builder()
                .name(s.getName())
                .price(s.getPrice())
                .category(s.getCategory())
                .build();
        model.addAttribute("dto", dto);
        model.addAttribute("id", id);
        return "admin/services/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam(value = "id", required = false) Long id,
                       @Valid @ModelAttribute("dto") PvzServiceDto dto,
                       BindingResult result,
                       Model model,
                       RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("id", id);
            return "admin/services/form";
        }
        PvzService entity = (id == null) ? new PvzService() : service.findById(id);
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setCategory(dto.getCategory());
        service.save(entity);
        ra.addFlashAttribute("success", id == null ? "Услуга добавлена" : "Услуга обновлена");
        return "redirect:/admin/services";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("success", "Услуга удалена");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Нельзя удалить: " + e.getMessage());
        }
        return "redirect:/admin/services";
    }
}