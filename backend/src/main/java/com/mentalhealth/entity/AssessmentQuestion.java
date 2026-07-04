package com.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.*;

@TableName("assessment_question")
public class AssessmentQuestion {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long scaleId;
    private String questionText;
    private String questionType;
    private Integer sortOrder;
    private String options;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getScaleId() { return scaleId; }
    public void setScaleId(Long scaleId) { this.scaleId = scaleId; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }
}
