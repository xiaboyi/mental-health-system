package com.mentalhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mentalhealth.entity.*;
import com.mentalhealth.mapper.*;
import com.mentalhealth.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    @Autowired private ChatSessionMapper sessionMapper;
    @Autowired private MessageMapper messageMapper;
    @Autowired private ConsultationRecordMapper recordMapper;

    @Override
    @Transactional
    public ChatSession getOrCreateSession(Long studentId, Long counselorId) {
        ChatSession session = sessionMapper.selectOne(new LambdaQueryWrapper<ChatSession>()
                .eq(ChatSession::getStudentId, studentId)
                .eq(ChatSession::getCounselorId, counselorId));
        if (session == null) {
            session = new ChatSession();
            session.setStudentId(studentId);
            session.setCounselorId(counselorId);
            session.setStatus("ACTIVE");
            sessionMapper.insert(session);
        }
        return session;
    }

    @Override
    public List<ChatSession> getUserSessions(Long userId) {
        return sessionMapper.selectByUserId(userId);
    }

    @Override
    public Message sendMessage(Message message) {
        message.setIsRead(0);
        message.setMsgType(message.getMsgType() != null ? message.getMsgType() : "TEXT");
        messageMapper.insert(message);
        return message;
    }

    @Override
    public List<Message> getSessionMessages(Long sessionId) {
        return messageMapper.selectBySessionId(sessionId);
    }

    @Override
    public int markAsRead(Long sessionId, Long userId) {
        return messageMapper.markAsRead(sessionId, userId);
    }

    @Override
    public int getUnreadCount(Long userId) {
        return messageMapper.countUnread(userId);
    }

    @Override
    public ConsultationRecord saveRecord(ConsultationRecord record) {
        recordMapper.insert(record);
        return record;
    }

    @Override
    public ConsultationRecord updateRecord(Long id, ConsultationRecord record) {
        ConsultationRecord exist = recordMapper.selectById(id);
        if (exist == null) return null;
        if (record.getContent() != null) exist.setContent(record.getContent());
        if (record.getEvaluation() != null) exist.setEvaluation(record.getEvaluation());
        if (record.getSuggestion() != null) exist.setSuggestion(record.getSuggestion());
        if (record.getRiskLevel() != null) exist.setRiskLevel(record.getRiskLevel());
        recordMapper.updateById(exist);
        return exist;
    }

    @Override
    public List<ConsultationRecord> getRecordsByStudentId(Long studentId) {
        return recordMapper.selectList(new LambdaQueryWrapper<ConsultationRecord>()
                .eq(ConsultationRecord::getStudentId, studentId)
                .orderByDesc(ConsultationRecord::getCreateTime));
    }

    @Override
    public List<ConsultationRecord> getRecordsByCounselorId(Long counselorId) {
        return recordMapper.selectList(new LambdaQueryWrapper<ConsultationRecord>()
                .eq(ConsultationRecord::getCounselorId, counselorId)
                .orderByDesc(ConsultationRecord::getCreateTime));
    }
}
