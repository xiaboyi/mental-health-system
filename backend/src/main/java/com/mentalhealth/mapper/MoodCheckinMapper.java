package com.mentalhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mentalhealth.entity.MoodCheckin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

public interface MoodCheckinMapper extends BaseMapper<MoodCheckin> {
    @Select("SELECT * FROM mood_checkin WHERE user_id = #{userId} AND checkin_date = CURDATE()")
    MoodCheckin selectTodayCheckin(@Param("userId") Long userId);

    @Select("SELECT * FROM mood_checkin WHERE user_id = #{userId} ORDER BY checkin_date DESC LIMIT 30")
    List<MoodCheckin> selectRecentByUserId(@Param("userId") Long userId);

    @Select("SELECT mood_type, COUNT(*) as count FROM mood_checkin GROUP BY mood_type")
    List<Map<String, Object>> countByMoodType();

    @Select("SELECT checkin_date as date, AVG(mood_score) as avg_score FROM mood_checkin WHERE checkin_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) GROUP BY checkin_date ORDER BY checkin_date")
    List<Map<String, Object>> weeklyMoodTrend();
}
