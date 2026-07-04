package com.mentalhealth.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mentalhealth.dto.PageResult;
import com.mentalhealth.dto.Result;
import com.mentalhealth.entity.Appointment;
import com.mentalhealth.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public Result<?> create(HttpServletRequest request, @RequestBody Appointment appointment) {
        Long userId = (Long) request.getAttribute("userId");
        appointment.setStudentId(userId);
        try {
            return Result.success(appointmentService.createAppointment(appointment));
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public Result<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> params) {
        return Result.success(appointmentService.updateStatus(id, params.get("status")));
    }

    @GetMapping("/my")
    public Result<?> getMyAppointments(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        if ("COUNSELOR".equals(role) || "ADMIN".equals(role)) {
            return Result.success(appointmentService.getCounselorAppointments(userId));
        }
        return Result.success(appointmentService.getStudentAppointments(userId));
    }

    @GetMapping("/all")
    public Result<?> getAll(HttpServletRequest request,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "15") int size,
                            @RequestParam(required = false) String status) {
        Page<Appointment> p = appointmentService.getAllAppointments(page, size, status);
        return Result.success(PageResult.of(p.getTotal(), p.getPages(), p.getCurrent(), p.getSize(), p.getRecords()));
    }

    @GetMapping("/check")
    public Result<?> checkAvailability(@RequestParam Long counselorId,
                                       @RequestParam String date,
                                       @RequestParam String timeSlot) {
        return Result.success(appointmentService.isTimeSlotAvailable(counselorId, date, timeSlot));
    }
}
