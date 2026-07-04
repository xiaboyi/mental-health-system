package com.mentalhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mentalhealth.entity.Appointment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

public interface AppointmentMapper extends BaseMapper<Appointment> {
    @Select("SELECT a.*, s.real_name as student_name, c.real_name as counselor_name, co.title as counselor_title FROM appointment a LEFT JOIN user s ON a.student_id = s.id LEFT JOIN user c ON a.counselor_id = c.id LEFT JOIN counselor co ON co.user_id = c.id WHERE a.student_id = #{userId} ORDER BY a.create_time DESC")
    List<Appointment> selectByStudentId(@Param("userId") Long userId);

    @Select("SELECT a.*, s.real_name as student_name, c.real_name as counselor_name, co.title as counselor_title FROM appointment a LEFT JOIN user s ON a.student_id = s.id LEFT JOIN user c ON a.counselor_id = c.id LEFT JOIN counselor co ON co.user_id = c.id WHERE a.counselor_id = #{userId} ORDER BY a.create_time DESC")
    List<Appointment> selectByCounselorId(@Param("userId") Long userId);

    @Select("SELECT status, COUNT(*) as count FROM appointment GROUP BY status")
    List<Map<String, Object>> countByStatus();

    @Select("SELECT DATE(appointment_date) as date, COUNT(*) as count FROM appointment WHERE appointment_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) GROUP BY DATE(appointment_date) ORDER BY date")
    List<Map<String, Object>> weeklyAppointmentCount();
}
