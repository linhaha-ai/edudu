package com.edudu.edu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.edudu.common.annotation.Excel;
import com.edudu.common.core.domain.BaseEntity;

/**
 * 故事互动问题对象 edu_story_interaction
 * 
 * @author edudu
 * @date 2026-04-07
 */
public class EduStoryInteraction extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 互动ID */
    private Long id;

    /** 故事ID */
    @Excel(name = "故事ID")
    private Long storyId;

    /** 互动问题 */
    @Excel(name = "互动问题")
    private String question;

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

    public void setStoryId(Long storyId) 
    {
        this.storyId = storyId;
    }

    public Long getStoryId() 
    {
        return storyId;
    }

    public void setQuestion(String question) 
    {
        this.question = question;
    }

    public String getQuestion() 
    {
        return question;
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
            .append("storyId", getStoryId())
            .append("question", getQuestion())
            .append("sort", getSort())
            .append("createTime", getCreateTime())
            .toString();
    }
}
