package com.edudu.edu.domain.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录请求体
 */
@Data
public class EduLoginBody implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录类型：phone(手机号+验证码)、wechat(微信)、account(账号密码)
     */
    private String loginType;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;

    /**
     * 密码
     */
    private String password;

    /**
     * 微信code
     */
    private String wechatCode;

    /**
     * 用户ID
     */
    private Long userId;
}
