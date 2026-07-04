package com.mentalhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mentalhealth.dto.LoginDTO;
import com.mentalhealth.dto.LoginResultDTO;
import com.mentalhealth.dto.RegisterDTO;
import com.mentalhealth.entity.User;
import com.mentalhealth.mapper.UserMapper;
import com.mentalhealth.service.UserService;
import com.mentalhealth.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtils jwtUtils;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public LoginResultDTO login(LoginDTO loginDTO) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginDTO.getUsername()));
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        if (!encoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());
        LoginResultDTO result = new LoginResultDTO();
        result.setToken(token);
        result.setUserId(user.getId());
        result.setUsername(user.getUsername());
        result.setRealName(user.getRealName());
        result.setRole(user.getRole());
        result.setAvatar(user.getAvatar());
        return result;
    }

    @Override
    public void register(RegisterDTO dto) {
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRealName(dto.getRealName() != null ? dto.getRealName() : dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setGender(dto.getGender() != null ? dto.getGender() : 0);
        user.setAge(dto.getAge());
        user.setRole(dto.getRole() != null ? dto.getRole() : "STUDENT");
        user.setStatus(1);
        baseMapper.insert(user);
    }

    @Override
    public User getCurrentUser(String token) {
        Long userId = jwtUtils.getUserId(token);
        if (userId == null) return null;
        return baseMapper.selectById(userId);
    }
}
