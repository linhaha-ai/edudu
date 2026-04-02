package com.edudu.edu.service;

import com.edudu.edu.domain.EduUser;
import com.edudu.edu.domain.model.EduLoginBody;
import com.edudu.edu.domain.model.EduRegisterBody;
import com.edudu.edu.domain.vo.EduUserInfoVO;

/**
 * 前端用户Service接口
 */
public interface IEduUserService {

    /**
     * 用户登录
     */
    String login(EduLoginBody loginBody);

    /**
     * 用户注册
     */
    Long register(EduRegisterBody registerBody);

    /**
     * 获取用户信息
     */
    EduUserInfoVO getUserInfo(Long userId);

    /**
     * 获取用户详情
     */
    EduUser getUserDetail(Long userId);

    /**
     * 更新用户信息
     */
    boolean updateUser(EduUser user);

    /**
     * 修改密码
     */
    boolean updatePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 发送验证码
     */
    boolean sendCode(String phone);

    /**
     * 重置密码
     */
    boolean resetPassword(String phone, String code, String newPassword);
}
