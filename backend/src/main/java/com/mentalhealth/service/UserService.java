package com.mentalhealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mentalhealth.dto.LoginDTO;
import com.mentalhealth.dto.LoginResultDTO;
import com.mentalhealth.dto.RegisterDTO;
import com.mentalhealth.entity.User;

public interface UserService extends IService<User> {
    LoginResultDTO login(LoginDTO loginDTO);
    void register(RegisterDTO registerDTO);
    User getCurrentUser(String token);
}
