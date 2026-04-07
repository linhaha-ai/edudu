package com.edudu.edu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.edudu.common.annotation.Excel;
import com.edudu.common.core.domain.BaseEntity;

/**
 * 故事记录对象 edu_story_record
 * 
 * @author edudu
 * @date 2026-04-07
 */
public class EduStoryRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 故事ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 孩子ID */
    @Excel(name = "孩子ID")
    private Long childId;

    /** 孩子年龄 */
    @Excel(name = "孩子年龄")
    private Long age;

    /** 故事场景（睡前/吃饭/发脾气等） */
    @Excel(name = "故事场景", readConverterExp = "睡=前/吃饭/发脾气等")
    private String scene;

    /** 教育目标（学会分享/勇敢等） */
    @Excel(name = "教育目标", readConverterExp = "学=会分享/勇敢等")
    private String goal;

    /** 故事标题 */
    @Excel(name = "故事标题")
    private String title;

    /** 故事内容 */
    @Excel(name = "故事内容")
    private String content;

    /** 教育意义 */
    @Excel(name = "教育意义")
    private String moral;

    /** AI模型类型 */
    @Excel(name = "AI模型类型")
    private String modelType;

    /** 消耗token数 */
    @Excel(name = "消耗token数")
    private Long tokensUsed;

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

    public void setAge(Long age) 
    {
        this.age = age;
    }

    public Long getAge() 
    {
        return age;
    }

    public void setScene(String scene) 
    {
        this.scene = scene;
    }

    public String getScene() 
    {
        return scene;
    }

    public void setGoal(String goal) 
    {
        this.goal = goal;
    }

    public String getGoal() 
    {
        return goal;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setMoral(String moral) 
    {
        this.moral = moral;
    }

    public String getMoral() 
    {
        return moral;
    }

    public void setModelType(String modelType) 
    {
        this.modelType = modelType;
    }

    public String getModelType() 
    {
        return modelType;
    }

    public void setTokensUsed(Long tokensUsed) 
    {
        this.tokensUsed = tokensUsed;
    }

    public Long getTokensUsed() 
    {
        return tokensUsed;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("childId", getChildId())
            .append("age", getAge())
            .append("scene", getScene())
            .append("goal", getGoal())
            .append("title", getTitle())
            .append("content", getContent())
            .append("moral", getMoral())
            .append("modelType", getModelType())
            .append("tokensUsed", getTokensUsed())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
