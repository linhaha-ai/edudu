package com.edudu.edu.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.edudu.common.annotation.Excel;
import com.edudu.common.core.domain.BaseEntity;

/**
 * 用户任务记录对象 edu_task_record
 * 
 * @author edudu
 * @date 2026-04-07
 */
public class EduTaskRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
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
    private String taskTitle;

    /** 状态（0未完成 1已完成） */
    @Excel(name = "状态", readConverterExp = "0=未完成,1=已完成")
    private String status;

    /** 完成反馈 */
    @Excel(name = "完成反馈")
    private String feedback;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completedAt;

    /** 记录日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记录日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date recordDate;

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

    public void setTaskTitle(String taskTitle) 
    {
        this.taskTitle = taskTitle;
    }

    public String getTaskTitle() 
    {
        return taskTitle;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setFeedback(String feedback) 
    {
        this.feedback = feedback;
    }

    public String getFeedback() 
    {
        return feedback;
    }

    public void setCompletedAt(Date completedAt) 
    {
        this.completedAt = completedAt;
    }

    public Date getCompletedAt() 
    {
        return completedAt;
    }

    public void setRecordDate(Date recordDate) 
    {
        this.recordDate = recordDate;
    }

    public Date getRecordDate() 
    {
        return recordDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("childId", getChildId())
            .append("taskId", getTaskId())
            .append("taskTitle", getTaskTitle())
            .append("status", getStatus())
            .append("feedback", getFeedback())
            .append("completedAt", getCompletedAt())
            .append("recordDate", getRecordDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
