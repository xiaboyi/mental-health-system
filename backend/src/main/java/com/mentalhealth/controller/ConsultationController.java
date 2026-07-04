package com.mentalhealth.controller;

import com.mentalhealth.dto.Result;
import com.mentalhealth.entity.ChatSession;
import com.mentalhealth.entity.ConsultationRecord;
import com.mentalhealth.entity.Message;
import com.mentalhealth.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/consultation")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @PostMapping("/session")
    public Result<?> getOrCreateSession(HttpServletRequest request, @RequestBody Map<String, Long> params) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        Long studentId, counselorId;
        if ("COUNSELOR".equals(role)) {
            studentId = params.get("studentId");
            counselorId = userId;
        } else {
            studentId = userId;
            counselorId = params.get("counselorId");
        }
        ChatSession session = consultationService.getOrCreateSession(studentId, counselorId);
        return Result.success(session);
    }

    @GetMapping("/sessions")
    public Result<?> getSessions(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(consultationService.getUserSessions(userId));
    }

    @PostMapping("/message")
    public Result<?> sendMessage(HttpServletRequest request, @RequestBody Message message) {
        Long userId = (Long) request.getAttribute("userId");
        message.setSenderId(userId);
        return Result.success(consultationService.sendMessage(message));
    }

    @GetMapping("/messages/{sessionId}")
    public Result<?> getMessages(@PathVariable Long sessionId) {
        return Result.success(consultationService.getSessionMessages(sessionId));
    }

    @PutMapping("/read/{sessionId}")
    public Result<?> markAsRead(@PathVariable Long sessionId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        consultationService.markAsRead(sessionId, userId);
        return Result.success("ok", null);
    }

    @GetMapping("/unread")
    public Result<?> getUnreadCount(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(consultationService.getUnreadCount(userId));
    }

    @PostMapping("/record")
    public Result<?> saveRecord(@RequestBody ConsultationRecord record) {
        return Result.success(consultationService.saveRecord(record));
    }

    @PutMapping("/record/{id}")
    public Result<?> updateRecord(@PathVariable Long id, @RequestBody ConsultationRecord record) {
        ConsultationRecord updated = consultationService.updateRecord(id, record);
        if (updated == null) return Result.error("记录不存在");
        return Result.success("更新成功", updated);
    }

    @GetMapping("/records")
    public Result<?> getRecords(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        if ("COUNSELOR".equals(role)) {
            return Result.success(consultationService.getRecordsByCounselorId(userId));
        }
        return Result.success(consultationService.getRecordsByStudentId(userId));
    }
}
