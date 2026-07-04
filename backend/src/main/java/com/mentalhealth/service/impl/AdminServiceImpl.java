package com.mentalhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mentalhealth.dto.StatDTO;
import com.mentalhealth.entity.SysLog;
import com.mentalhealth.mapper.*;
import com.mentalhealth.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired private UserMapper userMapper;
    @Autowired private AppointmentMapper appointmentMapper;
    @Autowired private ArticleMapper articleMapper;
    @Autowired private AssessmentRecordMapper assessmentRecordMapper;
    @Autowired private MoodCheckinMapper moodCheckinMapper;
    @Autowired private SysLogMapper sysLogMapper;

    @Override
    public StatDTO getEnhancedDashboard() {
        StatDTO stat = new StatDTO();
        stat.setTotalUsers(userMapper.selectCount(null));
        stat.setTotalCounselors(userMapper.countByRole("COUNSELOR"));
        stat.setTotalAppointments(appointmentMapper.selectCount(null));
        stat.setTotalArticles(Long.valueOf(articleMapper.selectCount(null)));
        stat.setPendingAppointments(appointmentMapper.selectCount(
                new LambdaQueryWrapper<com.mentalhealth.entity.Appointment>()
                        .eq(com.mentalhealth.entity.Appointment::getStatus, "PENDING")));
        stat.setTodayAppointments(appointmentMapper.selectCount(
                new LambdaQueryWrapper<com.mentalhealth.entity.Appointment>()
                        .eq(com.mentalhealth.entity.Appointment::getAppointmentDate, LocalDate.now())));
        stat.setMoodTrend(moodCheckinMapper.weeklyMoodTrend());
        stat.setAppointmentByStatus(appointmentMapper.countByStatus());
        stat.setAssessmentDistribution(assessmentRecordMapper.countByResultLevel());
        stat.setWeeklyAppointments(appointmentMapper.weeklyAppointmentCount());
        return stat;
    }

    @Override
    public Page<SysLog> getLogs(int page, int size, String username, String operation) {
        LambdaQueryWrapper<SysLog> wrapper = new LambdaQueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            wrapper.like(SysLog::getUsername, username);
        }
        if (operation != null && !operation.isEmpty()) {
            wrapper.like(SysLog::getOperation, operation);
        }
        wrapper.orderByDesc(SysLog::getCreateTime);
        return sysLogMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public Map<String, Object> getUserTrend() {
        Map<String, Object> result = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        for (int i = 29; i >= 0; i--) {
            LocalDate d = LocalDate.now().minusDays(i);
            dates.add(d.toString());
            counts.add(userMapper.selectCount(
                    new LambdaQueryWrapper<com.mentalhealth.entity.User>()
                            .le(com.mentalhealth.entity.User::getCreateTime, d.plusDays(1))));
        }
        result.put("dates", dates);
        result.put("counts", counts);
        return result;
    }

    @Override
    public Map<String, Object> getRoleDistribution() {
        Map<String, Object> result = new HashMap<>();
        result.put("STUDENT", userMapper.selectCount(
                new LambdaQueryWrapper<com.mentalhealth.entity.User>().eq(com.mentalhealth.entity.User::getRole, "STUDENT")));
        result.put("COUNSELOR", userMapper.selectCount(
                new LambdaQueryWrapper<com.mentalhealth.entity.User>().eq(com.mentalhealth.entity.User::getRole, "COUNSELOR")));
        result.put("ADMIN", userMapper.selectCount(
                new LambdaQueryWrapper<com.mentalhealth.entity.User>().eq(com.mentalhealth.entity.User::getRole, "ADMIN")));
        return result;
    }
}
