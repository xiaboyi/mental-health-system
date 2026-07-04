package com.mentalhealth.dto;

import java.util.List;
import java.util.Map;

public class StatDTO {
    private Long totalUsers;
    private Long totalCounselors;
    private Long totalAppointments;
    private Long totalArticles;
    private Long todayAppointments;
    private Long pendingAppointments;
    private List<Map<String, Object>> moodTrend;
    private List<Map<String, Object>> appointmentByStatus;
    private List<Map<String, Object>> assessmentDistribution;
    private List<Map<String, Object>> weeklyAppointments;
    private List<Map<String, Object>> counselorWorkload;

    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }
    public Long getTotalCounselors() { return totalCounselors; }
    public void setTotalCounselors(Long totalCounselors) { this.totalCounselors = totalCounselors; }
    public Long getTotalAppointments() { return totalAppointments; }
    public void setTotalAppointments(Long totalAppointments) { this.totalAppointments = totalAppointments; }
    public Long getTotalArticles() { return totalArticles; }
    public void setTotalArticles(Long totalArticles) { this.totalArticles = totalArticles; }
    public Long getTodayAppointments() { return todayAppointments; }
    public void setTodayAppointments(Long todayAppointments) { this.todayAppointments = todayAppointments; }
    public Long getPendingAppointments() { return pendingAppointments; }
    public void setPendingAppointments(Long pendingAppointments) { this.pendingAppointments = pendingAppointments; }
    public List<Map<String, Object>> getMoodTrend() { return moodTrend; }
    public void setMoodTrend(List<Map<String, Object>> moodTrend) { this.moodTrend = moodTrend; }
    public List<Map<String, Object>> getAppointmentByStatus() { return appointmentByStatus; }
    public void setAppointmentByStatus(List<Map<String, Object>> appointmentByStatus) { this.appointmentByStatus = appointmentByStatus; }
    public List<Map<String, Object>> getAssessmentDistribution() { return assessmentDistribution; }
    public void setAssessmentDistribution(List<Map<String, Object>> assessmentDistribution) { this.assessmentDistribution = assessmentDistribution; }
    public List<Map<String, Object>> getWeeklyAppointments() { return weeklyAppointments; }
    public void setWeeklyAppointments(List<Map<String, Object>> weeklyAppointments) { this.weeklyAppointments = weeklyAppointments; }
    public List<Map<String, Object>> getCounselorWorkload() { return counselorWorkload; }
    public void setCounselorWorkload(List<Map<String, Object>> counselorWorkload) { this.counselorWorkload = counselorWorkload; }
}
