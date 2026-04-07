package com.edudu.edu.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.edudu.common.annotation.Excel;
import com.edudu.common.core.domain.BaseEntity;

/**
 * 订阅计划配置对象 edu_subscription_plan
 * 
 * @author edudu
 * @date 2026-04-07
 */
public class EduSubscriptionPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 计划ID */
    private Long id;

    /** 计划代码（monthly/yearly） */
    @Excel(name = "计划代码", readConverterExp = "m=onthly/yearly")
    private String planCode;

    /** 计划名称（月卡/年卡） */
    @Excel(name = "计划名称", readConverterExp = "月=卡/年卡")
    private String planName;

    /** 价格 */
    @Excel(name = "价格")
    private BigDecimal price;

    /** 时长（天） */
    @Excel(name = "时长", readConverterExp = "天=")
    private Long durationDays;

    /** 功能权益（JSON数组） */
    @Excel(name = "功能权益", readConverterExp = "J=SON数组")
    private String features;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setPlanCode(String planCode) 
    {
        this.planCode = planCode;
    }

    public String getPlanCode() 
    {
        return planCode;
    }

    public void setPlanName(String planName) 
    {
        this.planName = planName;
    }

    public String getPlanName() 
    {
        return planName;
    }

    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }

    public void setDurationDays(Long durationDays) 
    {
        this.durationDays = durationDays;
    }

    public Long getDurationDays() 
    {
        return durationDays;
    }

    public void setFeatures(String features) 
    {
        this.features = features;
    }

    public String getFeatures() 
    {
        return features;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("planCode", getPlanCode())
            .append("planName", getPlanName())
            .append("price", getPrice())
            .append("durationDays", getDurationDays())
            .append("features", getFeatures())
            .append("status", getStatus())
            .append("sort", getSort())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
