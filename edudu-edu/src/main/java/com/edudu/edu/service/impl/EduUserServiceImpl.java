package com.edudu.edu.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edudu.common.constant.CacheConstants;
import com.edudu.common.constant.Constants;
import com.edudu.common.core.domain.BaseEntity;
import com.edudu.common.exception.ServiceException;
import com.edudu.common.utils.StringUtils;
import com.edudu.common.utils.servlet.ServletUtils;
import com.edudu.common.utils.ip.IpUtils;
import com.edudu.edu.domain.EduUser;
import com.edudu.edu.domain.model.EduLoginBody;
import com.edudu.edu.domain.model.EduRegisterBody;
import com.edudu.edu.domain.vo.EduUserInfoVO;
import com.edudu.edu.mapper.EduUserMapper;
import com.edudu.edu.service.IEduTokenService;
import com.edudu.edu.service.IEduUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 前端用户Service实现
 */
@Service
public class EduUserServiceImpl extends ServiceImpl<EduUserMapper, EduUser> implements IEduUserService {

    @Autowired
    private IEduTokenService tokenService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CODE_KEY = CacheConstants.CAPTCHA_CODE_KEY + "edu_phone:";
    private static final long CODE_EXPIRE = 5L; // 验证码5分钟过期

    /**
     * 用户登录
     */
    @Override
    public String login(EduLoginBody loginBody) {
        String loginType = loginBody.getLoginType();
        EduUser user;

        if ("phone".equals(loginType)) {
            // 手机号+验证码登录
            user = validateAndGetUserByPhone(loginBody.getPhone(), loginBody.getCode());
        } else if ("wechat".equals(loginType)) {
            // 微信登录
            user = getUserByWechat(loginBody.getWechatCode());
        } else {
            // 账号密码登录
            user = getUserByAccount(loginBody.getPhone(), loginBody.getPassword());
        }

        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        if ("1".equals(user.getStatus())) {
            throw new ServiceException("用户已被禁用");
        }

        // 更新登录信息
        HttpServletRequest request = ServletUtils.getRequest();
        user.setLoginIp(IpUtils.getIpAddr(request));
        user.setLoginDate(new Date());
        baseMapper.updateById(user);

        // 生成Token
        return tokenService.createToken(user);
    }

    /**
     * 验证手机号和验证码
     */
    private EduUser validateAndGetUserByPhone(String phone, String code) {
        EduUser user = baseMapper.selectByPhone(phone);
        if (user == null) {
            throw new ServiceException("用户不存在，请先注册");
        }

        // 验证验证码
        if (StringUtils.isNotEmpty(code)) {
            String cacheCode = (String) redisTemplate.opsForValue().get(CODE_KEY + phone);
            if (!code.equals(cacheCode)) {
                throw new ServiceException("验证码错误");
            }
            redisTemplate.delete(CODE_KEY + phone);
        }

        return user;
    }

    /**
     * 微信登录
     */
    private EduUser getUserByWechat(String wechatCode) {
        // TODO: 调用微信接口获取 openid 和 unionid
        // 这里先模拟返回
        String openId = "mock_openid_" + wechatCode;
        String unionId = "mock_unionid_" + wechatCode;

        // 查询用户
        EduUser user = baseMapper.selectByUnionId(unionId);
        if (user == null) {
            // 自动注册
            user = new EduUser();
            user.setUnionId(unionId);
            user.setOpenId(openId);
            user.setNickname("微信用户");
            user.setStatus("0");
            user.setCreateBy("wechat");
            baseMapper.insert(user);
        }

        return user;
    }

    /**
     * 账号密码登录
     */
    private EduUser getUserByAccount(String phone, String password) {
        EduUser user = baseMapper.selectByPhone(phone);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        if (StringUtils.isNotEmpty(user.getPassword())) {
            if (!password.equals(user.getPassword())) {
                // TODO: 使用密码加密验证
                // if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
                //     throw new ServiceException("密码错误");
                // }
            }
        }

        return user;
    }

    /**
     * 用户注册
     */
    @Override
    public Long register(EduRegisterBody registerBody) {
        // 验证验证码
        String cacheCode = (String) redisTemplate.opsForValue().get(CODE_KEY + registerBody.getPhone());
        if (StringUtils.isEmpty(cacheCode)) {
            throw new ServiceException("验证码已过期");
        }
        if (!cacheCode.equals(registerBody.getCode())) {
            throw new ServiceException("验证码错误");
        }

        // 检查手机号是否已存在
        EduUser existUser = baseMapper.selectByPhone(registerBody.getPhone());
        if (existUser != null) {
            throw new ServiceException("手机号已注册");
        }

        // 创建用户
        EduUser user = new EduUser();
        user.setPhone(registerBody.getPhone());
        user.setNickname(StringUtils.isNotEmpty(registerBody.getNickname()) 
            ? registerBody.getNickname() : "用户" + registerBody.getPhone().substring(5));
        
        if (StringUtils.isNotEmpty(registerBody.getPassword())) {
            user.setPassword(registerBody.getPassword()); // TODO: 加密
        }

        user.setStatus("0");
        user.setCreateBy("register");
        baseMapper.insert(user);

        // 删除验证码
        redisTemplate.delete(CODE_KEY + registerBody.getPhone());

        return user.getUserId();
    }

    /**
     * 获取用户信息
     */
    @Override
    public EduUserInfoVO getUserInfo(Long userId) {
        EduUser user = baseMapper.selectById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        EduUserInfoVO vo = new EduUserInfoVO();
        vo.setUserId(user.getUserId());
        vo.setPhone(user.getPhone());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setGender(user.getGender());
        vo.setBirthday(user.getBirthday());

        // TODO: 查询孩子数量和订阅状态
        vo.setChildCount(0);
        vo.setSubscriptionStatus("未订阅");

        return vo;
    }

    /**
     * 获取用户详情
     */
    @Override
    public EduUser getUserDetail(Long userId) {
        return baseMapper.selectById(userId);
    }

    /**
     * 更新用户信息
     */
    @Override
    public boolean updateUser(EduUser user) {
        user.setUserId(null);
        user.setPassword(null);
        user.setPhone(null);
        user.setUpdateBy("user");
        return baseMapper.updateById(user) > 0;
    }

    /**
     * 修改密码
     */
    @Override
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        EduUser user = baseMapper.selectById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        // 验证旧密码
        if (StringUtils.isNotEmpty(user.getPassword()) && !oldPassword.equals(user.getPassword())) {
            // TODO: 加密验证
            // if (!SecurityUtils.matchesPassword(oldPassword, user.getPassword())) {
            //     throw new ServiceException("原密码错误");
            // }
        }

        user.setPassword(newPassword); // TODO: 加密
        user.setUpdateBy("user");
        return baseMapper.updateById(user) > 0;
    }

    /**
     * 发送验证码
     */
    @Override
    public boolean sendCode(String phone) {
        // 模拟发送验证码（实际项目中调用短信服务）
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
        
        // 存入Redis，5分钟有效
        redisTemplate.opsForValue().set(CODE_KEY + phone, code, CODE_EXPIRE, TimeUnit.MINUTES);
        
        // TODO: 实际调用短信服务发送验证码
        System.out.println("【AI早教】验证码: " + code + "，5分钟内有效");
        
        return true;
    }

    /**
     * 重置密码
     */
    @Override
    public boolean resetPassword(String phone, String code, String newPassword) {
        // 验证验证码
        String cacheCode = (String) redisTemplate.opsForValue().get(CODE_KEY + phone);
        if (StringUtils.isEmpty(cacheCode)) {
            throw new ServiceException("验证码已过期");
        }
        if (!cacheCode.equals(code)) {
            throw new ServiceException("验证码错误");
        }

        // 更新密码
        EduUser user = baseMapper.selectByPhone(phone);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        user.setPassword(newPassword); // TODO: 加密
        user.setUpdateBy("reset");
        boolean result = baseMapper.updateById(user) > 0;

        // 删除验证码
        redisTemplate.delete(CODE_KEY + phone);

        return result;
    }
}
