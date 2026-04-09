package com.edudu.common.core.domain.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.edudu.common.annotation.Excel;
import com.edudu.common.core.domain.BaseEntity;

/**
 * 前端用户对象 edu_user
 * 
 * @author edudu
 * @date 2026-04-07
 */
public class EduUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 微信UnionID */
    @Excel(name = "微信UnionID")
    private String unionId;

    /** 微信OpenID */
    @Excel(name = "微信OpenID")
    private String openId;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickname;

    /** 头像 */
    @Excel(name = "头像")
    private String avatar;

    /** 性别（0未知 1男 2女） */
    @Excel(name = "性别", readConverterExp = "0=未知,1=男,2=女")
    private String gender;

    /** 生日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    /** 密码（可选，手机号登录可为空） */
    @Excel(name = "密码", readConverterExp = "可=选，手机号登录可为空")
    private String password;

    /** 状态（0正常 1禁用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=禁用")
    private String status;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    /** 最后登录IP */
    @Excel(name = "最后登录IP")
    private String loginIp;

    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loginDate;

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUnionId(String unionId) 
    {
        this.unionId = unionId;
    }

    public String getUnionId() 
    {
        return unionId;
    }

    public void setOpenId(String openId) 
    {
        this.openId = openId;
    }

    public String getOpenId() 
    {
        return openId;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setNickname(String nickname) 
    {
        this.nickname = nickname;
    }

    public String getNickname() 
    {
        return nickname;
    }

    public void setAvatar(String avatar) 
    {
        this.avatar = avatar;
    }

    public String getAvatar() 
    {
        return avatar;
    }

    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public String getGender() 
    {
        return gender;
    }

    public void setBirthday(Date birthday) 
    {
        this.birthday = birthday;
    }

    public Date getBirthday() 
    {
        return birthday;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public void setLoginIp(String loginIp) 
    {
        this.loginIp = loginIp;
    }

    public String getLoginIp() 
    {
        return loginIp;
    }

    public void setLoginDate(Date loginDate) 
    {
        this.loginDate = loginDate;
    }

    public Date getLoginDate() 
    {
        return loginDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("unionId", getUnionId())
            .append("openId", getOpenId())
            .append("phone", getPhone())
            .append("nickname", getNickname())
            .append("avatar", getAvatar())
            .append("gender", getGender())
            .append("birthday", getBirthday())
            .append("password", getPassword())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("loginIp", getLoginIp())
            .append("loginDate", getLoginDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
