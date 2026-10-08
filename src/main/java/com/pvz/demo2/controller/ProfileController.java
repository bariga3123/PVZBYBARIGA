package com.pvz.demo2.controller;

import com.pvz.demo2.dto.UserProfileDto;
import com.pvz.demo2.model.User;
import com.pvz.demo2.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    /** GET /profile — просмотр профиля. */
    @GetMapping
    public String viewProfile(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("user", user);
        return "profile/view";
    }

    /** GET /profile/edit — форма редактирования. */
    @GetMapping("/edit")
    public String editProfile(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName());

        UserProfileDto dto = UserProfileDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();

        model.addAttribute("dto", dto);
        return "profile/edit";
    }

    /** POST /profile/edit — сохранить изменения. */
    @PostMapping("/edit")
    public String saveProfile(@Valid @ModelAttribute("dto") UserProfileDto dto,
                              BindingResult result,
                              Authentication auth,
                              RedirectAttributes ra) {

        if (result.hasErrors()) {
            return "profile/edit";
        }

        try {
            userService.updateProfile(auth.getName(), dto.getEmail(), dto.getPhone());
            ra.addFlashAttribute("success", "Профиль обновлён");
            return "redirect:/profile";
        } catch (Exception e) {
            result.reject("error.dto", e.getMessage());
            return "profile/edit";
        }
    }
}