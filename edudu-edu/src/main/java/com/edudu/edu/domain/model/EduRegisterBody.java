package com.edudu.edu.domain.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 注册请求体
 */
@Data
public class EduRegisterBody implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 昵称
     */
    private String nickname;

    /**
     * 微信code
     */
    private String wechatCode;
}
