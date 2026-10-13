package com.pvz.demo2.controller;

import com.pvz.demo2.dto.OrderDto;
import com.pvz.demo2.model.Order;
import com.pvz.demo2.model.Parcel;
import com.pvz.demo2.model.User;
import com.pvz.demo2.service.CellService;
import com.pvz.demo2.service.CustomerService;
import com.pvz.demo2.service.OrderService;
import com.pvz.demo2.service.ParcelService;
import com.pvz.demo2.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {

    private final OrderService orderService;
    private final ParcelService parcelService;
    private final CellService cellService;
    private final CustomerService customerService;
    private final UserService userService;

    /** GET /requests — мои заявки. */
    @GetMapping
    public String myRequests(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName());
        List<Order> orders = orderService.findByUserId(user.getId());
        model.addAttribute("orders", orders);
        return "requests/list";
    }

    /** GET /requests/new — форма создания заявки. */
    @GetMapping("/new")
    public String newRequestForm(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName());

        // Посылки текущего пользователя, по которым ещё нет заявки
        List<Parcel> parcels = parcelService.findAll().stream()
                .filter(p -> p.getCustomer() != null
                        && p.getCustomer().getUser() != null
                        && p.getCustomer().getUser().getId().equals(user.getId()))
                .filter(p -> p.getOrder() == null)
                .toList();

        if (!model.containsAttribute("dto")) {
            model.addAttribute("dto", new OrderDto());
        }
        model.addAttribute("parcels", parcels);
        model.addAttribute("cells", cellService.findFree());
        return "requests/new";
    }

    /** POST /requests/new — создать заявку. */
    @PostMapping("/new")
    public String createRequest(@Valid @ModelAttribute("dto") OrderDto dto,
                                BindingResult result,
                                Authentication auth,
                                Model model,
                                RedirectAttributes ra) {

        User user = userService.findByUsername(auth.getName());

        if (result.hasErrors()) {
            // Перезаполняем списки, чтобы форма снова отрисовалась корректно
            List<Parcel> parcels = parcelService.findAll().stream()
                    .filter(p -> p.getCustomer() != null
                            && p.getCustomer().getUser() != null
                            && p.getCustomer().getUser().getId().equals(user.getId()))
                    .filter(p -> p.getOrder() == null)
                    .toList();
            model.addAttribute("parcels", parcels);
            model.addAttribute("cells", cellService.findFree());
            return "requests/new";
        }

        try {
            orderService.create(dto.getParcelId(), dto.getCellId(), dto.getComment(), auth.getName());
            ra.addFlashAttribute("success", "Заявка создана");
            return "redirect:/requests";
        } catch (Exception e) {
            result.reject("error.dto", e.getMessage());
            List<Parcel> parcels = parcelService.findAll().stream()
                    .filter(p -> p.getCustomer() != null
                            && p.getCustomer().getUser() != null
                            && p.getCustomer().getUser().getId().equals(user.getId()))
                    .filter(p -> p.getOrder() == null)
                    .toList();
            model.addAttribute("parcels", parcels);
            model.addAttribute("cells", cellService.findFree());
            return "requests/new";
        }
    }

    /** POST /requests/{id}/cancel — отмена заявки. */
    @PostMapping("/{id}/cancel")
    public String cancelRequest(@PathVariable Long id,
                                Authentication auth,
                                RedirectAttributes ra) {
        try {
            orderService.cancel(id, auth.getName());
            ra.addFlashAttribute("success", "Заявка отменена");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/requests";
    }
}