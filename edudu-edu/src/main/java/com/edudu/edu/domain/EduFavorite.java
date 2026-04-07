package com.edudu.edu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.edudu.common.annotation.Excel;
import com.edudu.common.core.domain.BaseEntity;

/**
 * 收藏对象 edu_favorite
 * 
 * @author edudu
 * @date 2026-04-07
 */
public class EduFavorite extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 收藏ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 孩子ID */
    @Excel(name = "孩子ID")
    private Long childId;

    /** 收藏类型（story故事/task任务） */
    @Excel(name = "收藏类型", readConverterExp = "s=tory故事/task任务")
    private String type;

    /** 来源ID（故事ID或任务ID） */
    @Excel(name = "来源ID", readConverterExp = "故=事ID或任务ID")
    private Long sourceId;

    /** 收藏标题 */
    @Excel(name = "收藏标题")
    private String title;

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

    public void setSourceId(Long sourceId) 
    {
        this.sourceId = sourceId;
    }

    public Long getSourceId() 
    {
        return sourceId;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("childId", getChildId())
            .append("type", getType())
            .append("sourceId", getSourceId())
            .append("title", getTitle())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
