package com.edudu.api.service;

import com.edudu.api.dto.UpdateUserBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.edudu.api.dto.EduRegisterBody;
import com.edudu.common.core.domain.model.EduLoginUser;
import com.edudu.common.constant.UserConstants;
import com.edudu.common.exception.ServiceException;
import com.edudu.common.utils.DateUtils;
import com.edudu.common.utils.MessageUtils;
import com.edudu.common.utils.StringUtils;
import com.edudu.common.utils.ip.IpUtils;
import com.edudu.common.core.domain.entity.EduUser;
import com.edudu.edu.service.IEduUserService;

/**
 * API登录校验方法
 * 
 * @author edudu
 */
@Component
public class EduLoginService {

    @Autowired
    private EduTokenService eduTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IEduUserService userService;

    /**
     * 登录验证
     */
    public String login(String phone, String password) {
        loginPreCheck(phone, password);
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phone, password);
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                throw new ServiceException(MessageUtils.message("user.password.not.match"));
            } else {
                throw new ServiceException(e.getMessage());
            }
        }
        EduLoginUser loginUser = (EduLoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        return eduTokenService.createToken(loginUser);
    }

    /**
     * 注册
     */
    public String register(EduRegisterBody registerBody) {
        String phone = registerBody.getPhone();
        String password = registerBody.getPassword();
        String nickname = registerBody.getNickname();

        if (StringUtils.isEmpty(phone)) {
            throw new ServiceException("手机号不能为空");
        }

        if (StringUtils.isEmpty(password) || password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            throw new ServiceException("密码长度必须在5到20个字符之间");
        }

        if (!userService.checkPhoneUnique(phone)) {
            throw new ServiceException("手机号已被注册");
        }

        EduUser user = new EduUser();
        user.setPhone(phone);
        user.setPassword(password);
        user.setNickname(StringUtils.isNotEmpty(nickname) ? nickname : "用户" + phone.substring(7));
        user.setStatus("0");
        user.setDelFlag("0");
        user.setCreateTime(DateUtils.getNowDate());

        int result = userService.insertEduUser(user);
        if (result <= 0) {
            throw new ServiceException("注册失败，请联系管理员");
        }

        return login(phone, password);
    }

    /**
     * 短信登录
     */
    public String loginBySms(String phone) {
        if (StringUtils.isEmpty(phone)) {
            throw new ServiceException("手机号不能为空");
        }
        if (phone.length() != 11) {
            throw new ServiceException("手机号格式不正确");
        }

        // 查询或创建用户
        EduUser user = userService.selectEduUserByPhone(phone);
        if (user == null) {
            // 自动注册：创建新用户
            user = new EduUser();
            user.setPhone(phone);
            user.setNickname("用户" + phone.substring(7));
            user.setStatus("0");
            user.setDelFlag("0");
            user.setCreateTime(DateUtils.getNowDate());
            userService.insertEduUser(user);
            user = userService.selectEduUserByPhone(phone);
        }

        // 创建登录用户
        EduLoginUser loginUser = new EduLoginUser();
        loginUser.setUser(user);
        loginUser.setUserId(user.getUserId());

        recordLoginInfo(user.getUserId());
        return eduTokenService.createToken(loginUser);
    }

    /**
     * 微信登录
     */
    public String loginByWechat(String openid, String nickname, String avatar) {
        if (StringUtils.isEmpty(openid)) {
            throw new ServiceException("微信授权失败");
        }

        // 查询或创建用户
        EduUser user = userService.selectEduUserByOpenid(openid);
        if (user == null) {
            // 自动注册
            user = new EduUser();
            user.setOpenId(openid);
            user.setNickname(StringUtils.isNotEmpty(nickname) ? nickname : "微信用户");
            user.setAvatar(StringUtils.isNotEmpty(avatar) ? avatar : "");
            user.setStatus("0");
            user.setDelFlag("0");
            user.setCreateTime(DateUtils.getNowDate());
            userService.insertEduUser(user);
            user = userService.selectEduUserByOpenid(openid);
        } else {
            // 更新昵称和头像
            if (StringUtils.isNotEmpty(nickname) || StringUtils.isNotEmpty(avatar)) {
                if (StringUtils.isNotEmpty(nickname)) {
                    user.setNickname(nickname);
                }
                if (StringUtils.isNotEmpty(avatar)) {
                    user.setAvatar(avatar);
                }
                userService.updateEduUser(user);
            }
        }

        EduLoginUser loginUser = new EduLoginUser();
        loginUser.setUser(user);
        loginUser.setUserId(user.getUserId());

        recordLoginInfo(user.getUserId());
        return eduTokenService.createToken(loginUser);
    }

    /**
     * 更新用户信息
     */
    public int updateUserInfo(Long userId, UpdateUserBody body) {
        EduUser user = userService.selectEduUserById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        if (StringUtils.isNotEmpty(body.getNickname())) {
            user.setNickname(body.getNickname());
        }
        if (StringUtils.isNotEmpty(body.getAvatar())) {
            user.setAvatar(body.getAvatar());
        }
        if (StringUtils.isNotEmpty(body.getGender())) {
            user.setGender(body.getGender());
        }
        if (body.getBirthday() != null) {
            user.setBirthday(body.getBirthday());
        }

        return userService.updateEduUser(user);
    }

    /**
     * 登录前置校验
     */
    public void loginPreCheck(String phone, String password) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)) {
            throw new ServiceException(MessageUtils.message("not.null"));
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            throw new ServiceException(MessageUtils.message("user.password.not.match"));
        }
        if (phone.length() != 11) {
            throw new ServiceException("手机号格式不正确");
        }
    }

    /**
     * 记录登录信息
     */
    public void recordLoginInfo(Long userId) {
        userService.updateLoginInfo(userId, IpUtils.getIpAddr(), DateUtils.getNowDate());
    }
}
