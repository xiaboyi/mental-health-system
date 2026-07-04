package com.mentalhealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mentalhealth.entity.AssessmentScale;
import com.mentalhealth.entity.AssessmentQuestion;
import com.mentalhealth.entity.AssessmentRecord;
import java.util.List;
import java.util.Map;

public interface AssessmentService extends IService<AssessmentScale> {
    List<AssessmentScale> getActiveScales();
    List<AssessmentQuestion> getQuestionsByScaleId(Long scaleId);
    AssessmentRecord submitAssessment(Long userId, Long scaleId, List<Map<String, Object>> answers);
    List<AssessmentRecord> getUserRecords(Long userId);
}
