package com.pvz.demo2.controller.admin;

import com.pvz.demo2.dto.CustomerDto;
import com.pvz.demo2.model.Customer;
import com.pvz.demo2.service.CustomerService;
import com.pvz.demo2.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final CustomerService customerService;
    private final UserService userService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", customerService.findAll());
        return "admin/customers/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("dto", new CustomerDto());
        model.addAttribute("id", null);
        model.addAttribute("users", userService.findAll());
        return "admin/customers/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Customer c = customerService.findById(id);
        CustomerDto dto = CustomerDto.builder()
                .fullName(c.getFullName())
                .address(c.getAddress())
                .phone(c.getPhone())
                .userId(c.getUser() != null ? c.getUser().getId() : null)
                .build();
        model.addAttribute("dto", dto);
        model.addAttribute("id", id);
        model.addAttribute("users", userService.findAll());
        return "admin/customers/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam(value = "id", required = false) Long id,
                       @Valid @ModelAttribute("dto") CustomerDto dto,
                       BindingResult result,
                       Model model,
                       RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("id", id);
            model.addAttribute("users", userService.findAll());
            return "admin/customers/form";
        }
        Customer entity = (id == null) ? new Customer() : customerService.findById(id);
        entity.setFullName(dto.getFullName());
        entity.setAddress(dto.getAddress());
        entity.setPhone(dto.getPhone());
        if (dto.getUserId() != null) {
            entity.setUser(userService.findById(dto.getUserId()));
        }
        customerService.save(entity);
        ra.addFlashAttribute("success", id == null ? "Получатель добавлен" : "Получатель обновлён");
        return "redirect:/admin/customers";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            customerService.delete(id);
            ra.addFlashAttribute("success", "Получатель удалён");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Нельзя удалить: " + e.getMessage());
        }
        return "redirect:/admin/customers";
    }
}