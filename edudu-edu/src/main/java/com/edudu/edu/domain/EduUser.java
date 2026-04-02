package com.edudu.edu.domain;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 前端用户对象 edu_user
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("edu_user")
public class EduUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /** 微信UnionID */
    private String unionId;

    /** 微信OpenID */
    private String openId;

    /** 手机号 */
    private String phone;

    /** 用户昵称 */
    private String nickname;

    /** 用户头像 */
    private String avatar;

    /** 性别（0未知 1男 2女） */
    private String gender;

    /** 生日 */
    private Date birthday;

    /** 密码 */
    private String password;

    /** 状态（0正常 1禁用） */
    private String status;

    /** 删除标志（0存在 2删除） */
    @TableLogic
    private String delFlag;

    /** 最后登录IP */
    private String loginIp;

    /** 最后登录时间 */
    private Date loginDate;

    /** 孩子列表（不存库） */
    @TableField(exist = false)
    private Object children;
}
