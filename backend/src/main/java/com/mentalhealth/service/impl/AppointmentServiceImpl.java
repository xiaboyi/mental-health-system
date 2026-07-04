package com.mentalhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mentalhealth.entity.Appointment;
import com.mentalhealth.mapper.AppointmentMapper;
import com.mentalhealth.service.AppointmentService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements AppointmentService {

    @Override
    public Appointment createAppointment(Appointment appointment) {
        if (!isTimeSlotAvailable(appointment.getCounselorId(),
                appointment.getAppointmentDate().toString(), appointment.getTimeSlot())) {
            throw new RuntimeException("该时间段已被预约，请选择其他时间");
        }
        appointment.setStatus("PENDING");
        baseMapper.insert(appointment);
        return appointment;
    }

    @Override
    public Appointment updateStatus(Long id, String status) {
        Appointment appointment = baseMapper.selectById(id);
        if (appointment == null) throw new RuntimeException("预约不存在");
        appointment.setStatus(status);
        baseMapper.updateById(appointment);
        return appointment;
    }

    @Override
    public List<Appointment> getStudentAppointments(Long studentId) {
        return baseMapper.selectByStudentId(studentId);
    }

    @Override
    public List<Appointment> getCounselorAppointments(Long counselorId) {
        return baseMapper.selectByCounselorId(counselorId);
    }

    @Override
    public Page<Appointment> getAllAppointments(int page, int size, String status) {
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Appointment::getStatus, status);
        }
        wrapper.orderByDesc(Appointment::getCreateTime);
        return baseMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public boolean isTimeSlotAvailable(Long counselorId, String date, String timeSlot) {
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<Appointment>()
                .eq(Appointment::getCounselorId, counselorId)
                .eq(Appointment::getAppointmentDate, date)
                .eq(Appointment::getTimeSlot, timeSlot)
                .eq(Appointment::getStatus, "CONFIRMED"));
        return count == 0;
    }
}
