package com.edudu.edu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.edudu.common.annotation.Excel;
import com.edudu.common.core.domain.BaseEntity;

/**
 * 家长问题对象 edu_parent_question
 * 
 * @author edudu
 * @date 2026-04-07
 */
public class EduParentQuestion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 问题ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 孩子ID */
    @Excel(name = "孩子ID")
    private Long childId;

    /** 问题描述 */
    @Excel(name = "问题描述")
    private String question;

    /** 状态（0待回答 1已回答） */
    @Excel(name = "状态", readConverterExp = "0=待回答,1=已回答")
    private String status;

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

    public void setQuestion(String question) 
    {
        this.question = question;
    }

    public String getQuestion() 
    {
        return question;
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
            .append("userId", getUserId())
            .append("childId", getChildId())
            .append("question", getQuestion())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
