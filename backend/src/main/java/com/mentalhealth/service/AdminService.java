package com.mentalhealth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mentalhealth.dto.StatDTO;
import com.mentalhealth.entity.SysLog;
import java.util.Map;

public interface AdminService {
    StatDTO getEnhancedDashboard();
    Page<SysLog> getLogs(int page, int size, String username, String operation);
    Map<String, Object> getUserTrend();
    Map<String, Object> getRoleDistribution();
}
