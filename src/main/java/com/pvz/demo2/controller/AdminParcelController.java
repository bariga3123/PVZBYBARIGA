package com.pvz.demo2.controller.admin;

import com.pvz.demo2.dto.ParcelDto;
import com.pvz.demo2.model.Parcel;
import com.pvz.demo2.service.CustomerService;
import com.pvz.demo2.service.ParcelService;
import com.pvz.demo2.service.PvzServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/parcels")
@RequiredArgsConstructor
public class AdminParcelController {

    private final ParcelService parcelService;
    private final CustomerService customerService;
    private final PvzServiceService pvzServiceService;

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q, Model model) {
        model.addAttribute("items", parcelService.search(q));
        model.addAttribute("q", q);
        return "admin/parcels/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("dto", new ParcelDto());
        model.addAttribute("id", null);
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("services", pvzServiceService.findAll());
        return "admin/parcels/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Parcel p = parcelService.findById(id);
        ParcelDto dto = ParcelDto.builder()
                .trackingNumber(p.getTrackingNumber())
                .description(p.getDescription())
                .weight(p.getWeight())
                .arrivalDate(p.getArrivalDate())
                .storageUntil(p.getStorageUntil())
                .customerId(p.getCustomer() != null ? p.getCustomer().getId() : null)
                .serviceId(p.getService() != null ? p.getService().getId() : null)
                .build();
        model.addAttribute("dto", dto);
        model.addAttribute("id", id);
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("services", pvzServiceService.findAll());
        return "admin/parcels/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam(value = "id", required = false) Long id,
                       @Valid @ModelAttribute("dto") ParcelDto dto,
                       BindingResult result,
                       Model model,
                       RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("id", id);
            model.addAttribute("customers", customerService.findAll());
            model.addAttribute("services", pvzServiceService.findAll());
            return "admin/parcels/form";
        }
        Parcel entity = (id == null) ? new Parcel() : parcelService.findById(id);
        entity.setTrackingNumber(dto.getTrackingNumber());
        entity.setDescription(dto.getDescription());
        entity.setWeight(dto.getWeight());
        entity.setArrivalDate(dto.getArrivalDate());
        entity.setStorageUntil(dto.getStorageUntil());
        entity.setCustomer(customerService.findById(dto.getCustomerId()));
        if (dto.getServiceId() != null) {
            entity.setService(pvzServiceService.findById(dto.getServiceId()));
        }
        parcelService.save(entity);
        ra.addFlashAttribute("success", id == null ? "Посылка добавлена" : "Посылка обновлена");
        return "redirect:/admin/parcels";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            parcelService.delete(id);
            ra.addFlashAttribute("success", "Посылка удалена");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Нельзя удалить: " + e.getMessage());
        }
        return "redirect:/admin/parcels";
    }
}