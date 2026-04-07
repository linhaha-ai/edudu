package com.edudu.edu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.edudu.common.annotation.Excel;
import com.edudu.common.core.domain.BaseEntity;

/**
 * AI回答对象 edu_parent_answer
 * 
 * @author edudu
 * @date 2026-04-07
 */
public class EduParentAnswer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 回答ID */
    private Long id;

    /** 问题ID */
    @Excel(name = "问题ID")
    private Long questionId;

    /** 问题原因分析 */
    @Excel(name = "问题原因分析")
    private String analysis;

    /** 解决方案（JSON数组） */
    @Excel(name = "解决方案", readConverterExp = "J=SON数组")
    private String solutions;

    /** 沟通示例 */
    @Excel(name = "沟通示例")
    private String example;

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

    public void setQuestionId(Long questionId) 
    {
        this.questionId = questionId;
    }

    public Long getQuestionId() 
    {
        return questionId;
    }

    public void setAnalysis(String analysis) 
    {
        this.analysis = analysis;
    }

    public String getAnalysis() 
    {
        return analysis;
    }

    public void setSolutions(String solutions) 
    {
        this.solutions = solutions;
    }

    public String getSolutions() 
    {
        return solutions;
    }

    public void setExample(String example) 
    {
        this.example = example;
    }

    public String getExample() 
    {
        return example;
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
            .append("questionId", getQuestionId())
            .append("analysis", getAnalysis())
            .append("solutions", getSolutions())
            .append("example", getExample())
            .append("modelType", getModelType())
            .append("tokensUsed", getTokensUsed())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
