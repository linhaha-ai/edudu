package com.edudu.api.service;

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
public class EduLoginService
{
    @Autowired
    private EduTokenService eduTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IEduUserService userService;

    /**
     * 登录验证
     * 
     * @param phone 手机号
     * @param password 密码
     * @return 结果
     */
    public String login(String phone, String password)
    {
        // 登录前置校验
        loginPreCheck(phone, password);
        // 用户验证
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phone, password);
            // 该方法会去调用EduUserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                throw new ServiceException(MessageUtils.message("user.password.not.match"));
            }
            else
            {
                throw new ServiceException(e.getMessage());
            }
        }
        EduLoginUser loginUser = (EduLoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return eduTokenService.createToken(loginUser);
    }

    /**
     * 注册
     * 
     * @param registerBody 注册信息
     * @return 结果
     */
    public String register(EduRegisterBody registerBody)
    {
        String phone = registerBody.getPhone();
        String password = registerBody.getPassword();
        String nickname = registerBody.getNickname();

        // 校验手机号格式
        if (StringUtils.isEmpty(phone))
        {
            throw new ServiceException("手机号不能为空");
        }
        
        // 校验密码格式
        if (StringUtils.isEmpty(password) || password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            throw new ServiceException("密码长度必须在5到20个字符之间");
        }

        // 检查手机号是否唯一
        if (!userService.checkPhoneUnique(phone))
        {
            throw new ServiceException("手机号已被注册");
        }

        // 创建用户
        EduUser user = new EduUser();
        user.setPhone(phone);
        user.setPassword(password); // 密码会在 SecurityConfig 的 BCryptPasswordEncoder 加密
        user.setNickname(StringUtils.isNotEmpty(nickname) ? nickname : "用户" + phone.substring(7));
        user.setStatus("0"); // 正常状态
        user.setDelFlag("0"); // 未删除
        user.setCreateTime(DateUtils.getNowDate());

        int result = userService.insertEduUser(user);
        if (result <= 0)
        {
            throw new ServiceException("注册失败，请联系管理员");
        }

        // 注册成功后自动登录
        return login(phone, password);
    }

    /**
     * 登录前置校验
     * @param phone 手机号
     * @param password 用户密码
     */
    public void loginPreCheck(String phone, String password)
    {
        // 手机号或密码为空 错误
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password))
        {
            throw new ServiceException(MessageUtils.message("not.null"));
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            throw new ServiceException(MessageUtils.message("user.password.not.match"));
        }
        // 手机号不在指定范围内 错误
        if (phone.length() != 11)
        {
            throw new ServiceException("手机号格式不正确");
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        userService.updateLoginInfo(userId, IpUtils.getIpAddr(), DateUtils.getNowDate());
    }
}
