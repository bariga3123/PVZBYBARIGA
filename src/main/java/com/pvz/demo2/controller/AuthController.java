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

    /** GET /login — показать форму входа. */
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

    /** GET /register — показать форму регистрации. */
    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("dto")) {
            model.addAttribute("dto", new UserRegistrationDto());
        }
        return "auth/register";
    }

    /** POST /register — обработать регистрацию. */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("dto") UserRegistrationDto dto,
                           BindingResult result,
                           RedirectAttributes ra) {

        // 1. Проверка совпадения паролей
        if (dto.getPassword() != null && !dto.getPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.dto", "Пароли не совпадают");
        }

        // 2. Валидация не прошла — возвращаем форму
        if (result.hasErrors()) {
            return "auth/register";
        }

        // 3. Пытаемся зарегистрировать
        try {
            userService.register(dto);
            ra.addFlashAttribute("success", "Регистрация успешна! Войдите в систему.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            // например, "Логин уже занят" или "Email уже используется"
            result.reject("error.dto", e.getMessage());
            return "auth/register";
        }
    }
}