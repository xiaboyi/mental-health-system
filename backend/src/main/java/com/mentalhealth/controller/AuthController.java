package com.mentalhealth.controller;

import com.mentalhealth.dto.LoginDTO;
import com.mentalhealth.dto.RegisterDTO;
import com.mentalhealth.dto.Result;
import com.mentalhealth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            return Result.success(userService.login(loginDTO));
        } catch (RuntimeException e) {
            return Result.error(401, e.getMessage());
        }
    }

    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        try {
            userService.register(registerDTO);
            return Result.success("注册成功", null);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
}

