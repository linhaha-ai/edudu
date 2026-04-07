package com.edudu.edu.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.edudu.common.annotation.Excel;
import com.edudu.common.core.domain.BaseEntity;

/**
 * 孩子信息对象 edu_child
 * 
 * @author edudu
 * @date 2026-04-07
 */
public class EduChild extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 孩子ID */
    private Long childId;

    /** 所属用户ID */
    @Excel(name = "所属用户ID")
    private Long userId;

    /** 孩子姓名 */
    @Excel(name = "孩子姓名")
    private String name;

    /** 性别（0男 1女） */
    @Excel(name = "性别", readConverterExp = "0=男,1=女")
    private String gender;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthDate;

    /** 头像地址 */
    @Excel(name = "头像地址")
    private String avatar;

    /** 性格特点 */
    @Excel(name = "性格特点")
    private String personality;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public void setChildId(Long childId) 
    {
        this.childId = childId;
    }

    public Long getChildId() 
    {
        return childId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public String getGender() 
    {
        return gender;
    }

    public void setBirthDate(Date birthDate) 
    {
        this.birthDate = birthDate;
    }

    public Date getBirthDate() 
    {
        return birthDate;
    }

    public void setAvatar(String avatar) 
    {
        this.avatar = avatar;
    }

    public String getAvatar() 
    {
        return avatar;
    }

    public void setPersonality(String personality) 
    {
        this.personality = personality;
    }

    public String getPersonality() 
    {
        return personality;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("childId", getChildId())
            .append("userId", getUserId())
            .append("name", getName())
            .append("gender", getGender())
            .append("birthDate", getBirthDate())
            .append("avatar", getAvatar())
            .append("personality", getPersonality())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
