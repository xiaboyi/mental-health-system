package com.mentalhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@TableName("counselor")
public class Counselor {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private String specialty;
    private String description;
    private String certification;
    private Integer workYears;
    private Integer maxDailySlots;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String realName;
    @TableField(exist = false)
    private String phone;
    @TableField(exist = false)
    private String email;
    @TableField(exist = false)
    private String avatar;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCertification() { return certification; }
    public void setCertification(String certification) { this.certification = certification; }
    public Integer getWorkYears() { return workYears; }
    public void setWorkYears(Integer workYears) { this.workYears = workYears; }
    public Integer getMaxDailySlots() { return maxDailySlots; }
    public void setMaxDailySlots(Integer maxDailySlots) { this.maxDailySlots = maxDailySlots; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
