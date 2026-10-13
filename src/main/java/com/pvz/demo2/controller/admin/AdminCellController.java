package com.pvz.demo2.controller.admin;

import com.pvz.demo2.dto.CellDto;
import com.pvz.demo2.model.Cell;
import com.pvz.demo2.model.CellSize;
import com.pvz.demo2.service.CellService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/cells")
@RequiredArgsConstructor
public class AdminCellController {

    private final CellService cellService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", cellService.findAll());
        return "admin/cells/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("dto", new CellDto());
        model.addAttribute("id", null);
        model.addAttribute("sizes", CellSize.values());
        return "admin/cells/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Cell c = cellService.findById(id);
        CellDto dto = CellDto.builder()
                .cellNumber(c.getCellNumber())
                .size(c.getSize())
                .isOccupied(c.getIsOccupied())
                .build();
        model.addAttribute("dto", dto);
        model.addAttribute("id", id);
        model.addAttribute("sizes", CellSize.values());
        return "admin/cells/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam(value = "id", required = false) Long id,
                       @Valid @ModelAttribute("dto") CellDto dto,
                       BindingResult result,
                       Model model,
                       RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("id", id);
            model.addAttribute("sizes", CellSize.values());
            return "admin/cells/form";
        }
        Cell entity = (id == null) ? new Cell() : cellService.findById(id);
        entity.setCellNumber(dto.getCellNumber());
        entity.setSize(dto.getSize());
        entity.setIsOccupied(dto.getIsOccupied() != null && dto.getIsOccupied());
        cellService.save(entity);
        ra.addFlashAttribute("success", id == null ? "Ячейка добавлена" : "Ячейка обновлена");
        return "redirect:/admin/cells";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            cellService.delete(id);
            ra.addFlashAttribute("success", "Ячейка удалена");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Нельзя удалить: " + e.getMessage());
        }
        return "redirect:/admin/cells";
    }
}