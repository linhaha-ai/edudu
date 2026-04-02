package com.edudu.edu.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.edudu.common.constant.CacheConstants;
import com.edudu.common.utils.StringUtils;
import com.edudu.edu.domain.EduUser;
import com.edudu.edu.service.IEduTokenService;
import com.edudu.common.utils.SecurityUtils;
import com.edudu.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Token管理Service实现
 */
@Service
public class EduTokenServiceImpl implements IEduTokenService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_KEY = CacheConstants.LOGIN_TOKEN_KEY + "edu:";
    private static final long TOKEN_EXPIRE = 7L; // 7天过期

    /**
     * 创建Token
     */
    @Override
    public String createToken(EduUser user) {
        // 生成Token
        String token = UUID.fastUUID().toString(true);

        // 构建用户缓存信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", user.getUserId());
        userInfo.put("phone", user.getPhone());
        userInfo.put("nickname", user.getNickname());

        // 存入Redis
        String cacheKey = TOKEN_KEY + token;
        redisTemplate.opsForValue().set(cacheKey, userInfo, TOKEN_EXPIRE, TimeUnit.DAYS);

        // 返回Token（前端使用）
        return token;
    }

    /**
     * 验证Token
     */
    @Override
    @SuppressWarnings("unchecked")
    public Long verifyToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        String cacheKey = TOKEN_KEY + token;
        Map<String, Object> userInfo = (Map<String, Object>) redisTemplate.opsForValue().get(cacheKey);

        if (ObjectUtil.isNull(userInfo)) {
            return null;
        }

        // 刷新Token有效期
        redisTemplate.expire(cacheKey, TOKEN_EXPIRE, TimeUnit.DAYS);

        return (Long) userInfo.get("userId");
    }

    /**
     * 删除Token
     */
    @Override
    public void deleteToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String cacheKey = TOKEN_KEY + token;
            redisTemplate.delete(cacheKey);
        }
    }

    /**
     * 获取Token中的用户ID
     */
    @Override
    public Long getUserIdByToken(String token) {
        return verifyToken(token);
    }
}
