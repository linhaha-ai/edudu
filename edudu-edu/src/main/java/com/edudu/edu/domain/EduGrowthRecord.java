package com.edudu.edu.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.edudu.common.annotation.Excel;
import com.edudu.common.core.domain.BaseEntity;

/**
 * 成长记录对象 edu_growth_record
 * 
 * @author edudu
 * @date 2026-04-07
 */
public class EduGrowthRecord extends BaseEntity
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

    /** 记录类型（sleep睡眠/diet饮食/learning学习/health健康） */
    @Excel(name = "记录类型", readConverterExp = "s=leep睡眠/diet饮食/learning学习/health健康")
    private String type;

    /** 记录值 */
    @Excel(name = "记录值")
    private String value;

    /** 单位（如：小时、次、个） */
    @Excel(name = "单位", readConverterExp = "如=：小时、次、个")
    private String unit;

    /** 备注说明 */
    @Excel(name = "备注说明")
    private String note;

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

    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    public void setValue(String value) 
    {
        this.value = value;
    }

    public String getValue() 
    {
        return value;
    }

    public void setUnit(String unit) 
    {
        this.unit = unit;
    }

    public String getUnit() 
    {
        return unit;
    }

    public void setNote(String note) 
    {
        this.note = note;
    }

    public String getNote() 
    {
        return note;
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
            .append("type", getType())
            .append("value", getValue())
            .append("unit", getUnit())
            .append("note", getNote())
            .append("recordDate", getRecordDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
