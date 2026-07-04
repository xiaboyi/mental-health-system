package com.mentalhealth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mentalhealth.entity.Appointment;
import java.util.List;

public interface AppointmentService extends IService<Appointment> {
    Appointment createAppointment(Appointment appointment);
    Appointment updateStatus(Long id, String status);
    List<Appointment> getStudentAppointments(Long studentId);
    List<Appointment> getCounselorAppointments(Long counselorId);
    Page<Appointment> getAllAppointments(int page, int size, String status);
    boolean isTimeSlotAvailable(Long counselorId, String date, String timeSlot);
}
