package com.mentalhealth.controller;

import com.mentalhealth.dto.Result;
import com.mentalhealth.entity.Counselor;
import com.mentalhealth.entity.MoodCheckin;
import com.mentalhealth.service.CounselorService;
import com.mentalhealth.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private CounselorService counselorService;

    @GetMapping("/dashboard")
    public Result<?> getDashboard() {
        return Result.success(statisticsService.getDashboardStats());
    }

    @GetMapping("/counselor-dashboard")
    public Result<?> getCounselorDashboard(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Counselor counselor = counselorService.getByUserId(userId);
        if (counselor == null) return Result.error("咨询师信息不存在");
        return Result.success(statisticsService.getCounselorDashboard(counselor.getUserId()));
    }

    @GetMapping("/mood/history")
    public Result<?> getMoodHistory(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(statisticsService.getUserMoodHistory(userId));
    }

    @PostMapping("/mood/checkin")
    public Result<?> checkin(HttpServletRequest request, @RequestBody MoodCheckin checkin) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(statisticsService.checkinToday(userId, checkin));
    }

    @GetMapping("/mood/today")
    public Result<?> getTodayMood(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(statisticsService.getTodayMood(userId));
    }

    // 咨询师查看指定来访者的情绪历史
    @GetMapping("/mood/student/{studentId}")
    public Result<?> getStudentMoodHistory(@PathVariable Long studentId) {
        return Result.success(statisticsService.getUserMoodHistory(studentId));
    }
}
