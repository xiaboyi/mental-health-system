package com.mentalhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mentalhealth.entity.AssessmentRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

public interface AssessmentRecordMapper extends BaseMapper<AssessmentRecord> {
    @Select("SELECT ar.*, as2.name as scale_name, u.real_name FROM assessment_record ar LEFT JOIN assessment_scale as2 ON ar.scale_id = as2.id LEFT JOIN user u ON ar.user_id = u.id WHERE ar.user_id = #{userId} ORDER BY ar.create_time DESC")
    List<AssessmentRecord> selectByUserIdWithDetail(@Param("userId") Long userId);

    @Select("SELECT result_level, COUNT(*) as count FROM assessment_record GROUP BY result_level")
    List<Map<String, Object>> countByResultLevel();
}
