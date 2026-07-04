package com.mentalhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mentalhealth.entity.AssessmentQuestion;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface AssessmentQuestionMapper extends BaseMapper<AssessmentQuestion> {
    @Select("SELECT * FROM assessment_question WHERE scale_id = #{scaleId} ORDER BY sort_order")
    List<AssessmentQuestion> selectByScaleId(Long scaleId);
}
