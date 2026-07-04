package com.mentalhealth.service;

import com.mentalhealth.dto.StatDTO;
import com.mentalhealth.entity.MoodCheckin;
import java.util.List;
import java.util.Map;

public interface StatisticsService {
    StatDTO getDashboardStats();
    Map<String, Object> getCounselorDashboard(Long counselorUserId);
    List<MoodCheckin> getUserMoodHistory(Long userId);
    MoodCheckin checkinToday(Long userId, MoodCheckin checkin);
    MoodCheckin getTodayMood(Long userId);
}
