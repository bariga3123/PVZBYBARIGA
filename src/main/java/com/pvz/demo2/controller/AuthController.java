package com.pvz.demo2.controller;

import com.pvz.demo2.dto.UserRegistrationDto;
import com.pvz.demo2.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Неверный логин или пароль");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Вы вышли из системы");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("dto")) {
            model.addAttribute("dto", new UserRegistrationDto());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("dto") UserRegistrationDto dto,
                           BindingResult result,
                           RedirectAttributes ra,
                           Model model) {

        // 1. Пароли должны совпадать
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            result.rejectValue("password", "error.dto", "Пароль обязателен");
        } else if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.dto", "Пароли не совпадают");
        }

        // 2. Валидация не прошла — вернуть форму
        if (result.hasErrors()) {
            return "auth/register";
        }

        // 3. Попытка регистрации
        try {
            userService.register(dto);
            ra.addFlashAttribute("success", "Регистрация успешна! Войдите в систему.");
            return "redirect:/login";
        } catch (Exception e) {
            // ЛЮБАЯ ошибка — показать её в форме сверху
            result.reject("error.dto", "Ошибка: " + e.getMessage());
            return "auth/register";
        }
    }
}