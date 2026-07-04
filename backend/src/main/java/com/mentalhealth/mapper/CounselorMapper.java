package com.mentalhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mentalhealth.entity.Counselor;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface CounselorMapper extends BaseMapper<Counselor> {
    @Select("SELECT c.*, u.real_name, u.phone, u.email, u.avatar FROM counselor c LEFT JOIN user u ON c.user_id = u.id WHERE u.deleted = 0")
    List<Counselor> selectAllWithUser();
}
