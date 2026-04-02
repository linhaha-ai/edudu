package com.edudu.edu.service;

import com.edudu.edu.domain.EduUser;

/**
 * Token管理Service接口
 */
public interface IEduTokenService {

    /**
     * 创建Token
     */
    String createToken(EduUser user);

    /**
     * 验证Token
     */
    Long verifyToken(String token);

    /**
     * 删除Token
     */
    void deleteToken(String token);

    /**
     * 获取Token中的用户ID
     */
    Long getUserIdByToken(String token);
}
