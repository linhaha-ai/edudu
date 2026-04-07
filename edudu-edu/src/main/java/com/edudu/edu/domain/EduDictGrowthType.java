package com.edudu.edu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.edudu.common.annotation.Excel;
import com.edudu.common.core.domain.BaseEntity;

/**
 * 成长记录类型字典对象 edu_dict_growth_type
 * 
 * @author edudu
 * @date 2026-04-07
 */
public class EduDictGrowthType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 字典ID */
    private Long id;

    /** 字典标签 */
    @Excel(name = "字典标签")
    private String dictLabel;

    /** 字典键值 */
    @Excel(name = "字典键值")
    private String dictValue;

    /** 默认单位 */
    @Excel(name = "默认单位")
    private String dictUnit;

    /** 字典排序 */
    @Excel(name = "字典排序")
    private Long dictSort;

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

    public void setDictLabel(String dictLabel) 
    {
        this.dictLabel = dictLabel;
    }

    public String getDictLabel() 
    {
        return dictLabel;
    }

    public void setDictValue(String dictValue) 
    {
        this.dictValue = dictValue;
    }

    public String getDictValue() 
    {
        return dictValue;
    }

    public void setDictUnit(String dictUnit) 
    {
        this.dictUnit = dictUnit;
    }

    public String getDictUnit() 
    {
        return dictUnit;
    }

    public void setDictSort(Long dictSort) 
    {
        this.dictSort = dictSort;
    }

    public Long getDictSort() 
    {
        return dictSort;
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
            .append("dictLabel", getDictLabel())
            .append("dictValue", getDictValue())
            .append("dictUnit", getDictUnit())
            .append("dictSort", getDictSort())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
