package com.edudu.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * API用户信息响应对象
 * 
 * @author edudu
 */
@Setter
@Getter
public class EduLoginUserVo
{
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别（0未知 1男 2女）
     */
    private String gender;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 访问令牌
     */
    private String token;

}
