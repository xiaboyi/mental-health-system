package com.mentalhealth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mentalhealth.dto.Result;
import com.mentalhealth.entity.Counselor;
import com.mentalhealth.service.CounselorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/counselor")
public class CounselorController {

    @Autowired
    private CounselorService counselorService;

    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(counselorService.getAllCounselors());
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.success(counselorService.getDetail(id));
    }

    @PostMapping
    public Result<?> create(@RequestBody Counselor counselor) {
        counselorService.save(counselor);
        return Result.success(counselor);
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Counselor counselor) {
        counselor.setId(id);
        counselorService.updateById(counselor);
        return Result.success("更新成功", null);
    }

    @GetMapping("/clients")
    public Result<?> getClients(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Counselor counselor = counselorService.getByUserId(userId);
        if (counselor == null) return Result.error("咨询师信息不存在");
        return Result.success(counselorService.getClients(counselor.getId()));
    }
}
