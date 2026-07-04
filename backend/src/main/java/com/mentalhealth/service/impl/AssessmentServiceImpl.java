package com.mentalhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mentalhealth.entity.*;
import com.mentalhealth.mapper.*;
import com.mentalhealth.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class AssessmentServiceImpl extends ServiceImpl<AssessmentScaleMapper, AssessmentScale> implements AssessmentService {

    @Autowired
    private AssessmentQuestionMapper questionMapper;
    @Autowired
    private AssessmentRecordMapper recordMapper;
    @Autowired
    private AssessmentAnswerMapper answerMapper;

    @Override
    public List<AssessmentScale> getActiveScales() {
        return baseMapper.selectList(new LambdaQueryWrapper<AssessmentScale>()
                .eq(AssessmentScale::getStatus, 1));
    }

    @Override
    public List<AssessmentQuestion> getQuestionsByScaleId(Long scaleId) {
        return questionMapper.selectByScaleId(scaleId);
    }

    @Override
    @Transactional
    public AssessmentRecord submitAssessment(Long userId, Long scaleId, List<Map<String, Object>> answers) {
        AssessmentScale scale = baseMapper.selectById(scaleId);
        if (scale == null) throw new RuntimeException("量表不存在");

        int rawScore = 0;
        AssessmentRecord record = new AssessmentRecord();
        record.setUserId(userId);
        record.setScaleId(scaleId);
        record.setTotalScore(0);
        record.setResultLevel("NORMAL");
        recordMapper.insert(record);

        // Calculate score for each answer
        for (Map<String, Object> answer : answers) {
            Long questionId = Long.valueOf(answer.get("questionId").toString());
            String selectedOption = (String) answer.get("selectedOption");
            AssessmentAnswer ans = new AssessmentAnswer();
            ans.setRecordId(record.getId());
            ans.setQuestionId(questionId);
            ans.setSelectedOption(selectedOption);

            AssessmentQuestion question = questionMapper.selectById(questionId);
            if (question != null && question.getOptions() != null) {
                JSONArray options = JSON.parseArray(question.getOptions());
                for (int i = 0; i < options.size(); i++) {
                    JSONObject opt = options.getJSONObject(i);
                    if (selectedOption.equals(opt.getString("label"))) {
                        int score = opt.getIntValue("score");
                        ans.setScore(score);
                        rawScore += score;
                        break;
                    }
                }
            }
            answerMapper.insert(ans);
        }

        // Determine result level with clinical thresholds
        String scaleType = scale.getType();
        int finalScore;
        String resultLevel;
        String resultDetail;

        if ("ANXIETY".equals(scaleType) || "DEPRESSION".equals(scaleType)) {
            // SAS/SDS: raw score × 1.25 = standard score (range 25-100)
            finalScore = (int) Math.round(rawScore * 1.25);

            if (finalScore < 50) {
                resultLevel = "NORMAL";
                resultDetail = "您的标准分为" + finalScore + "分（原始分" + rawScore + "分），处于正常范围。当前心理状态良好，请继续保持积极健康的生活方式。";
            } else if (finalScore < 60) {
                resultLevel = "MILD";
                resultDetail = "您的标准分为" + finalScore + "分（原始分" + rawScore + "分），提示可能存在轻度症状。建议适当关注自身情绪变化，保持规律作息和适度运动，可尝试放松训练。";
            } else if (finalScore < 70) {
                resultLevel = "MODERATE";
                resultDetail = "您的标准分为" + finalScore + "分（原始分" + rawScore + "分），提示可能存在中度症状。建议您重视心理健康，考虑预约学校心理咨询师进行专业评估和指导。";
            } else {
                resultLevel = "SEVERE";
                resultDetail = "您的标准分为" + finalScore + "分（原始分" + rawScore + "分），提示可能存在重度症状。强烈建议您尽快联系学校心理咨询中心或专业心理医生，寻求及时的帮助和支持。";
            }
        } else {
            // STRESS questionnaire: use raw score directly (range 20-80)
            finalScore = rawScore;

            if (finalScore <= 40) {
                resultLevel = "NORMAL";
                resultDetail = "您的得分为" + finalScore + "分，压力水平处于正常范围。您能够较好地应对当前的大学生活，请继续保持。";
            } else if (finalScore <= 55) {
                resultLevel = "MILD";
                resultDetail = "您的得分为" + finalScore + "分，存在轻度的压力感受。建议适当调整学习和生活节奏，增加运动和社交活动。";
            } else if (finalScore <= 70) {
                resultLevel = "MODERATE";
                resultDetail = "您的得分为" + finalScore + "分，压力水平偏高。建议您尝试时间管理技巧，必要时可向心理咨询师寻求帮助。";
            } else {
                resultLevel = "SEVERE";
                resultDetail = "您的得分为" + finalScore + "分，压力水平很高。强烈建议您联系心理咨询中心，获取专业的压力管理指导。";
            }
        }

        record.setTotalScore(finalScore);
        record.setResultLevel(resultLevel);
        record.setResultDetail(resultDetail);
        recordMapper.updateById(record);

        return record;
    }

    @Override
    public List<AssessmentRecord> getUserRecords(Long userId) {
        return recordMapper.selectByUserIdWithDetail(userId);
    }
}
