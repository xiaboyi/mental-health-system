package com.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mentalhealth.dto.PageResult;
import com.mentalhealth.dto.Result;
import com.mentalhealth.entity.User;
import com.mentalhealth.service.UserService;
import com.mentalhealth.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/info")
    public Result<?> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        User user = userService.getCurrentUser(token);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }

    @PutMapping("/info")
    public Result<?> updateInfo(HttpServletRequest request, @RequestBody User user) {
        Long userId = (Long) request.getAttribute("userId");
        user.setId(userId);
        user.setPassword(null);
        user.setUsername(null);
        user.setRole(null);
        user.setStatus(null);
        userService.updateById(user);
        return Result.success("更新成功", null);
    }

    @PutMapping("/password")
    public Result<?> updatePassword(HttpServletRequest request, @RequestBody java.util.Map<String, String> params) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userService.getById(userId);
        if (user == null) return Result.error("用户不存在");
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder =
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        if (!encoder.matches(params.get("oldPassword"), user.getPassword())) {
            return Result.error(400, "原密码错误");
        }
        user.setPassword(encoder.encode(params.get("newPassword")));
        userService.updateById(user);
        return Result.success("密码修改成功", null);
    }

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) String role) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (role != null && !role.isEmpty()) {
            wrapper.eq(User::getRole, role);
        }
        wrapper.orderByDesc(User::getCreateTime);
        Page<User> p = userService.page(new Page<>(page, size), wrapper);
        p.getRecords().forEach(u -> u.setPassword(null));
        return Result.success(PageResult.of(p.getTotal(), p.getPages(), p.getCurrent(), p.getSize(), p.getRecords()));
    }

    @PutMapping("/status/{id}")
    public Result<?> updateStatus(@PathVariable Long id, @RequestBody java.util.Map<String, Integer> params) {
        User user = userService.getById(id);
        if (user == null) return Result.error("用户不存在");
        user.setStatus(params.get("status"));
        userService.updateById(user);
        return Result.success("状态更新成功", null);
    }
}

