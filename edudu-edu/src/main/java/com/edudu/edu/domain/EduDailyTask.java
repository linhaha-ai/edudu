package com.edudu.edu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.edudu.common.annotation.Excel;
import com.edudu.common.core.domain.BaseEntity;

/**
 * 每日任务模板对象 edu_daily_task
 * 
 * @author edudu
 * @date 2026-04-07
 */
public class EduDailyTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务模板ID */
    private Long id;

    /** 适用年龄下限 */
    @Excel(name = "适用年龄下限")
    private Long ageMin;

    /** 适用年龄上限 */
    @Excel(name = "适用年龄上限")
    private Long ageMax;

    /** 任务主题（颜色/动物/情绪/语言） */
    @Excel(name = "任务主题", readConverterExp = "颜=色/动物/情绪/语言")
    private String theme;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String title;

    /** 训练目标 */
    @Excel(name = "训练目标")
    private String goal;

    /** 操作步骤（JSON格式） */
    @Excel(name = "操作步骤", readConverterExp = "J=SON格式")
    private String steps;

    /** 家长注意事项 */
    @Excel(name = "家长注意事项")
    private String tips;

    /** 建议时长 */
    @Excel(name = "建议时长")
    private String duration;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setAgeMin(Long ageMin) 
    {
        this.ageMin = ageMin;
    }

    public Long getAgeMin() 
    {
        return ageMin;
    }

    public void setAgeMax(Long ageMax) 
    {
        this.ageMax = ageMax;
    }

    public Long getAgeMax() 
    {
        return ageMax;
    }

    public void setTheme(String theme) 
    {
        this.theme = theme;
    }

    public String getTheme() 
    {
        return theme;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setGoal(String goal) 
    {
        this.goal = goal;
    }

    public String getGoal() 
    {
        return goal;
    }

    public void setSteps(String steps) 
    {
        this.steps = steps;
    }

    public String getSteps() 
    {
        return steps;
    }

    public void setTips(String tips) 
    {
        this.tips = tips;
    }

    public String getTips() 
    {
        return tips;
    }

    public void setDuration(String duration) 
    {
        this.duration = duration;
    }

    public String getDuration() 
    {
        return duration;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ageMin", getAgeMin())
            .append("ageMax", getAgeMax())
            .append("theme", getTheme())
            .append("title", getTitle())
            .append("goal", getGoal())
            .append("steps", getSteps())
            .append("tips", getTips())
            .append("duration", getDuration())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
