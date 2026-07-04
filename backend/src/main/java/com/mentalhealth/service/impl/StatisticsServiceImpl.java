package com.mentalhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mentalhealth.dto.StatDTO;
import com.mentalhealth.entity.*;
import com.mentalhealth.mapper.*;
import com.mentalhealth.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired private UserMapper userMapper;
    @Autowired private AppointmentMapper appointmentMapper;
    @Autowired private ArticleMapper articleMapper;
    @Autowired private AssessmentRecordMapper assessmentRecordMapper;
    @Autowired private MoodCheckinMapper moodCheckinMapper;

    @Override
    public StatDTO getDashboardStats() {
        StatDTO stat = new StatDTO();
        stat.setTotalUsers(userMapper.selectCount(null));
        stat.setTotalCounselors(userMapper.countByRole("COUNSELOR"));
        stat.setTotalAppointments(appointmentMapper.selectCount(null));
        stat.setTotalArticles(Long.valueOf(articleMapper.selectCount(null)));
        stat.setPendingAppointments(appointmentMapper.selectCount(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getStatus, "PENDING")));
        stat.setTodayAppointments(appointmentMapper.selectCount(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getAppointmentDate, LocalDate.now())));
        stat.setMoodTrend(moodCheckinMapper.weeklyMoodTrend());
        stat.setAppointmentByStatus(appointmentMapper.countByStatus());
        stat.setAssessmentDistribution(assessmentRecordMapper.countByResultLevel());
        stat.setWeeklyAppointments(appointmentMapper.weeklyAppointmentCount());
        return stat;
    }

    @Override
    public Map<String, Object> getCounselorDashboard(Long counselorUserId) {
        Map<String, Object> data = new HashMap<>();

        // My appointments count
        long myAppts = appointmentMapper.selectCount(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getCounselorId, counselorUserId));
        data.put("myAppointments", myAppts);

        // My pending appointments
        long pending = appointmentMapper.selectCount(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getCounselorId, counselorUserId)
                        .eq(Appointment::getStatus, "PENDING"));
        data.put("pendingAppointments", pending);

        // My completed appointments (today)
        long todayCompleted = appointmentMapper.selectCount(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getCounselorId, counselorUserId)
                        .eq(Appointment::getStatus, "COMPLETED")
                        .eq(Appointment::getAppointmentDate, LocalDate.now()));
        data.put("todayCompleted", todayCompleted);

        // My unique clients count
        List<Appointment> allMy = appointmentMapper.selectList(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getCounselorId, counselorUserId));
        long uniqueClients = allMy.stream().map(Appointment::getStudentId).distinct().count();
        data.put("uniqueClients", uniqueClients);

        // Weekly appointments for this counselor
        List<Map<String, Object>> weekly = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate d = LocalDate.now().minusDays(i);
            Map<String, Object> day = new HashMap<>();
            day.put("date", d.toString());
            long count = appointmentMapper.selectCount(
                    new LambdaQueryWrapper<Appointment>()
                            .eq(Appointment::getCounselorId, counselorUserId)
                            .eq(Appointment::getAppointmentDate, d));
            day.put("count", count);
            weekly.add(day);
        }
        data.put("weeklyAppointments", weekly);

        return data;
    }

    @Override
    public List<MoodCheckin> getUserMoodHistory(Long userId) {
        return moodCheckinMapper.selectRecentByUserId(userId);
    }

    @Override
    public MoodCheckin checkinToday(Long userId, MoodCheckin checkin) {
        MoodCheckin existing = moodCheckinMapper.selectTodayCheckin(userId);
        if (existing != null) {
            existing.setMoodType(checkin.getMoodType());
            existing.setMoodScore(checkin.getMoodScore());
            existing.setNote(checkin.getNote());
            moodCheckinMapper.updateById(existing);
            return existing;
        }
        checkin.setUserId(userId);
        checkin.setCheckinDate(LocalDate.now());
        moodCheckinMapper.insert(checkin);
        return checkin;
    }

    @Override
    public MoodCheckin getTodayMood(Long userId) {
        return moodCheckinMapper.selectTodayCheckin(userId);
    }
}
