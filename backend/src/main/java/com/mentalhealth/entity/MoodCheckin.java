package com.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("mood_checkin")
public class MoodCheckin {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String moodType;
    private Integer moodScore;
    private String note;
    private LocalDate checkinDate;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String realName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getMoodType() { return moodType; }
    public void setMoodType(String moodType) { this.moodType = moodType; }
    public Integer getMoodScore() { return moodScore; }
    public void setMoodScore(Integer moodScore) { this.moodScore = moodScore; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public LocalDate getCheckinDate() { return checkinDate; }
    public void setCheckinDate(LocalDate checkinDate) { this.checkinDate = checkinDate; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
}
