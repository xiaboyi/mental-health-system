package com.mentalhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mentalhealth.entity.ChatSession;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface ChatSessionMapper extends BaseMapper<ChatSession> {
    @Select("SELECT cs.*, s.real_name as student_name, c.real_name as counselor_name, " +
            "(SELECT content FROM message WHERE session_id = cs.id ORDER BY create_time DESC LIMIT 1) as last_message, " +
            "(SELECT create_time FROM message WHERE session_id = cs.id ORDER BY create_time DESC LIMIT 1) as last_message_time " +
            "FROM chat_session cs " +
            "LEFT JOIN user s ON cs.student_id = s.id " +
            "LEFT JOIN user c ON cs.counselor_id = c.id " +
            "WHERE (cs.student_id = #{userId} OR cs.counselor_id = #{userId}) AND cs.status = 'ACTIVE' " +
            "ORDER BY cs.update_time DESC")
    List<ChatSession> selectByUserId(@Param("userId") Long userId);
}
