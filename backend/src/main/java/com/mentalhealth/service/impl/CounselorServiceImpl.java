package com.mentalhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mentalhealth.entity.*;
import com.mentalhealth.mapper.*;
import com.mentalhealth.service.CounselorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CounselorServiceImpl extends ServiceImpl<CounselorMapper, Counselor> implements CounselorService {

    @Autowired private UserMapper userMapper;
    @Autowired private AppointmentMapper appointmentMapper;
    @Autowired private AssessmentRecordMapper assessmentRecordMapper;
    @Autowired private MoodCheckinMapper moodCheckinMapper;

    @Override
    public List<Counselor> getAllCounselors() {
        return baseMapper.selectAllWithUser();
    }

    @Override
    public Counselor getByUserId(Long userId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<Counselor>()
                .eq(Counselor::getUserId, userId));
    }

    @Override
    public Counselor getDetail(Long id) {
        Counselor counselor = baseMapper.selectById(id);
        if (counselor == null) return null;
        List<Counselor> list = baseMapper.selectAllWithUser();
        return list.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(counselor);
    }

    @Override
    public List<Map<String, Object>> getClients(Long counselorId) {
        // Get counselor's user_id
        Counselor counselor = baseMapper.selectById(counselorId);
        if (counselor == null) return new ArrayList<>();

        // Get all students who had appointments with this counselor
        List<Appointment> appointments = appointmentMapper.selectList(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getCounselorId, counselor.getUserId())
                        .orderByDesc(Appointment::getCreateTime));

        // Deduplicate by studentId and collect client info
        Map<Long, Map<String, Object>> clientMap = new LinkedHashMap<>();
        for (Appointment a : appointments) {
            if (!clientMap.containsKey(a.getStudentId())) {
                User student = userMapper.selectById(a.getStudentId());
                if (student == null) continue;

                Map<String, Object> client = new HashMap<>();
                client.put("studentId", student.getId());
                client.put("studentName", student.getRealName());
                client.put("username", student.getUsername());
                client.put("phone", student.getPhone());
                client.put("gender", student.getGender());
                client.put("age", student.getAge());

                // Count total appointments
                long totalAppts = appointmentMapper.selectCount(
                        new LambdaQueryWrapper<Appointment>()
                                .eq(Appointment::getStudentId, a.getStudentId())
                                .eq(Appointment::getCounselorId, counselor.getUserId()));
                client.put("totalAppointments", totalAppts);
                client.put("lastAppointmentDate", a.getAppointmentDate());

                // Get latest assessment result's risk level
                List<AssessmentRecord> records = assessmentRecordMapper.selectList(
                        new LambdaQueryWrapper<AssessmentRecord>()
                                .eq(AssessmentRecord::getUserId, student.getId())
                                .orderByDesc(AssessmentRecord::getCreateTime)
                                .last("LIMIT 1"));
                client.put("riskLevel", records.isEmpty() ? null : records.get(0).getResultLevel());

                // Get latest mood
                MoodCheckin latestMood = moodCheckinMapper.selectTodayCheckin(student.getId());
                client.put("latestMood", latestMood != null ? latestMood.getMoodScore() : null);
                client.put("latestMoodType", latestMood != null ? latestMood.getMoodType() : null);

                clientMap.put(a.getStudentId(), client);
            }
        }
        return new ArrayList<>(clientMap.values());
    }
}
