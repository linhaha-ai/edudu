package com.edudu.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * API用户注册对象
 * 
 * @author edudu
 */
@Setter
@Getter
public class EduRegisterBody
{
    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 验证码（可选）
     */
    private String code;

    /**
     * 唯一标识（可选）
     */
    private String uuid;

}
