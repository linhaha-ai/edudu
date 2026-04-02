package com.edudu.edu.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息VO
 */
@Data
public class EduUserInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 手机号 */
    private String phone;

    /** 用户昵称 */
    private String nickname;

    /** 用户头像 */
    private String avatar;

    /** 性别 */
    private String gender;

    /** 生日 */
    private Date birthday;

    /** 孩子数量 */
    private Integer childCount;

    /** 订阅状态 */
    private String subscriptionStatus;

    /** 订阅到期时间 */
    private Date subscriptionExpireTime;
}
