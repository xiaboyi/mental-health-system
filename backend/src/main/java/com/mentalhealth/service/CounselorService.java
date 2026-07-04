package com.mentalhealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mentalhealth.entity.Counselor;
import java.util.List;
import java.util.Map;

public interface CounselorService extends IService<Counselor> {
    List<Counselor> getAllCounselors();
    Counselor getByUserId(Long userId);
    Counselor getDetail(Long id);
    List<Map<String, Object>> getClients(Long counselorId);
}
