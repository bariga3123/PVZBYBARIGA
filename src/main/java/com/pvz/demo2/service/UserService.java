package com.pvz.demo2.service;

import com.pvz.demo2.dto.UserRegistrationDto;
import com.pvz.demo2.exception.ResourceNotFoundException;
import com.pvz.demo2.model.Role;
import com.pvz.demo2.model.User;
import com.pvz.demo2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден: " + username));
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден: id=" + id));
    }

    /** Регистрация нового пользователя. Пароль хешируется BCrypt. */
    @Transactional
    public User register(UserRegistrationDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Логин уже занят");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email уже используется");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public User updateProfile(String username, String email, String phone) {
        User user = findByUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        return userRepository.save(user);
    }
}