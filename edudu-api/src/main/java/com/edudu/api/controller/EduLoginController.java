package com.edudu.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.edudu.api.dto.EduLoginBody;
import com.edudu.api.dto.EduLoginUserVo;
import com.edudu.api.dto.EduRegisterBody;
import com.edudu.api.security.EduLoginUser;
import com.edudu.api.service.EduLoginService;
import com.edudu.api.service.EduTokenService;
import com.edudu.common.constant.Constants;
import com.edudu.common.core.domain.AjaxResult;
import com.edudu.common.utils.SecurityUtils;
import com.edudu.edu.domain.EduUser;

/**
 * API用户登录控制器
 * 
 * @author edudu
 */
@RestController
@RequestMapping("/auth")
public class EduLoginController
{
    @Autowired
    private EduLoginService eduLoginService;

    @Autowired
    private EduTokenService eduTokenService;

    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody EduLoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = eduLoginService.login(loginBody.getPhone(), loginBody.getPassword());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 注册方法
     * 
     * @param registerBody 注册信息
     * @return 结果
     */
    @PostMapping("/register")
    public AjaxResult register(@RequestBody EduRegisterBody registerBody)
    {
        AjaxResult ajax = AjaxResult.success("注册成功");
        // 注册并生成令牌
        String token = eduLoginService.register(registerBody);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("/info")
    public AjaxResult getInfo()
    {
        EduLoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser instanceof EduLoginUser)
        {
            EduUser user = ((EduLoginUser) loginUser).getUser();
            EduLoginUserVo userVo = new EduLoginUserVo();
            userVo.setUserId(user.getUserId());
            userVo.setPhone(user.getPhone());
            userVo.setNickname(user.getNickname());
            userVo.setAvatar(user.getAvatar());
            userVo.setGender(user.getGender());
            userVo.setBirthday(user.getBirthday());
            return AjaxResult.success(userVo);
        }
        return AjaxResult.error("获取用户信息失败");
    }
}
