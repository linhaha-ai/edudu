package com.edudu.edu.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.edudu.common.annotation.Excel;
import com.edudu.common.core.domain.BaseEntity;

/**
 * 每日任务分配对象 edu_task_daily
 * 
 * @author edudu
 * @date 2026-04-07
 */
public class EduTaskDaily extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分配ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 孩子ID */
    @Excel(name = "孩子ID")
    private Long childId;

    /** 任务模板ID */
    @Excel(name = "任务模板ID")
    private Long taskId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String title;

    /** 训练目标 */
    @Excel(name = "训练目标")
    private String goal;

    /** 操作步骤 */
    @Excel(name = "操作步骤")
    private String steps;

    /** 注意事项 */
    @Excel(name = "注意事项")
    private String tips;

    /** 建议时长 */
    @Excel(name = "建议时长")
    private String duration;

    /** 任务日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "任务日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date taskDate;

    /** 状态（0未完成 1已完成） */
    @Excel(name = "状态", readConverterExp = "0=未完成,1=已完成")
    private String status;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completedAt;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setChildId(Long childId) 
    {
        this.childId = childId;
    }

    public Long getChildId() 
    {
        return childId;
    }

    public void setTaskId(Long taskId) 
    {
        this.taskId = taskId;
    }

    public Long getTaskId() 
    {
        return taskId;
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

    public void setTaskDate(Date taskDate) 
    {
        this.taskDate = taskDate;
    }

    public Date getTaskDate() 
    {
        return taskDate;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setCompletedAt(Date completedAt) 
    {
        this.completedAt = completedAt;
    }

    public Date getCompletedAt() 
    {
        return completedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("childId", getChildId())
            .append("taskId", getTaskId())
            .append("title", getTitle())
            .append("goal", getGoal())
            .append("steps", getSteps())
            .append("tips", getTips())
            .append("duration", getDuration())
            .append("taskDate", getTaskDate())
            .append("status", getStatus())
            .append("completedAt", getCompletedAt())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
