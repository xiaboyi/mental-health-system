package com.mentalhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mentalhealth.entity.Message;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

public interface MessageMapper extends BaseMapper<Message> {
    @Select("SELECT * FROM message WHERE session_id = #{sessionId} ORDER BY create_time ASC")
    List<Message> selectBySessionId(@Param("sessionId") Long sessionId);

    @Update("UPDATE message SET is_read = 1 WHERE session_id = #{sessionId} AND receiver_id = #{userId} AND is_read = 0")
    int markAsRead(@Param("sessionId") Long sessionId, @Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM message WHERE receiver_id = #{userId} AND is_read = 0")
    int countUnread(@Param("userId") Long userId);
}
