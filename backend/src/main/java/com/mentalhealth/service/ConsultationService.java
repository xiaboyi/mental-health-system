package com.mentalhealth.service;

import com.mentalhealth.entity.ChatSession;
import com.mentalhealth.entity.ConsultationRecord;
import com.mentalhealth.entity.Message;
import java.util.List;

public interface ConsultationService {
    ChatSession getOrCreateSession(Long studentId, Long counselorId);
    List<ChatSession> getUserSessions(Long userId);
    Message sendMessage(Message message);
    List<Message> getSessionMessages(Long sessionId);
    int markAsRead(Long sessionId, Long userId);
    int getUnreadCount(Long userId);
    ConsultationRecord saveRecord(ConsultationRecord record);
    ConsultationRecord updateRecord(Long id, ConsultationRecord record);
    List<ConsultationRecord> getRecordsByStudentId(Long studentId);
    List<ConsultationRecord> getRecordsByCounselorId(Long counselorId);
}
