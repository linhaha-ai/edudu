package com.edudu.edu.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.edudu.common.annotation.Excel;
import com.edudu.common.core.domain.BaseEntity;

/**
 * 成长报告对象 edu_growth_report
 * 
 * @author edudu
 * @date 2026-04-07
 */
public class EduGrowthReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报告ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 孩子ID */
    @Excel(name = "孩子ID")
    private Long childId;

    /** 报告周期（如：2026年3月） */
    @Excel(name = "报告周期", readConverterExp = "如=：2026年3月")
    private String period;

    /** 总体评价 */
    @Excel(name = "总体评价")
    private String summary;

    /** 优点（JSON数组） */
    @Excel(name = "优点", readConverterExp = "J=SON数组")
    private String strengths;

    /** 建议（JSON数组） */
    @Excel(name = "建议", readConverterExp = "J=SON数组")
    private String improvements;

    /** 家长建议 */
    @Excel(name = "家长建议")
    private String advice;

    /** 报告日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报告日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reportDate;

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

    public void setPeriod(String period) 
    {
        this.period = period;
    }

    public String getPeriod() 
    {
        return period;
    }

    public void setSummary(String summary) 
    {
        this.summary = summary;
    }

    public String getSummary() 
    {
        return summary;
    }

    public void setStrengths(String strengths) 
    {
        this.strengths = strengths;
    }

    public String getStrengths() 
    {
        return strengths;
    }

    public void setImprovements(String improvements) 
    {
        this.improvements = improvements;
    }

    public String getImprovements() 
    {
        return improvements;
    }

    public void setAdvice(String advice) 
    {
        this.advice = advice;
    }

    public String getAdvice() 
    {
        return advice;
    }

    public void setReportDate(Date reportDate) 
    {
        this.reportDate = reportDate;
    }

    public Date getReportDate() 
    {
        return reportDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("childId", getChildId())
            .append("period", getPeriod())
            .append("summary", getSummary())
            .append("strengths", getStrengths())
            .append("improvements", getImprovements())
            .append("advice", getAdvice())
            .append("reportDate", getReportDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
