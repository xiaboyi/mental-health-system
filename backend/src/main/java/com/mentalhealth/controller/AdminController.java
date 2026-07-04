package com.mentalhealth.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mentalhealth.dto.PageResult;
import com.mentalhealth.dto.Result;
import com.mentalhealth.entity.SysLog;
import com.mentalhealth.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/dashboard")
    public Result<?> dashboard() {
        return Result.success(adminService.getEnhancedDashboard());
    }

    @GetMapping("/logs")
    public Result<?> logs(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "20") int size,
                          @RequestParam(required = false) String username,
                          @RequestParam(required = false) String operation) {
        Page<SysLog> p = adminService.getLogs(page, size, username, operation);
        return Result.success(PageResult.of(p.getTotal(), p.getPages(), p.getCurrent(), p.getSize(), p.getRecords()));
    }

    @GetMapping("/user-trend")
    public Result<?> userTrend() {
        return Result.success(adminService.getUserTrend());
    }

    @GetMapping("/role-distribution")
    public Result<?> roleDistribution() {
        return Result.success(adminService.getRoleDistribution());
    }
}
