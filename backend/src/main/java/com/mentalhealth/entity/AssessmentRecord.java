package com.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@TableName("assessment_record")
public class AssessmentRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long scaleId;
    private Integer totalScore;
    private String resultLevel;
    private String resultDetail;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String scaleName;
    @TableField(exist = false)
    private String realName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getScaleId() { return scaleId; }
    public void setScaleId(Long scaleId) { this.scaleId = scaleId; }
    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }
    public String getResultLevel() { return resultLevel; }
    public void setResultLevel(String resultLevel) { this.resultLevel = resultLevel; }
    public String getResultDetail() { return resultDetail; }
    public void setResultDetail(String resultDetail) { this.resultDetail = resultDetail; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getScaleName() { return scaleName; }
    public void setScaleName(String scaleName) { this.scaleName = scaleName; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
}
