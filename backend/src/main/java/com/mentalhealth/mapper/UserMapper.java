package com.mentalhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mentalhealth.entity.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT COUNT(*) FROM user WHERE role = #{role} AND deleted = 0 AND status = 1")
    Long countByRole(String role);
}
