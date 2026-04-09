package com.edudu.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.edudu.api.dto.EduLoginBody;
import com.edudu.api.dto.EduLoginUserVo;
import com.edudu.api.dto.EduRegisterBody;
import com.edudu.api.dto.SmsLoginBody;
import com.edudu.api.dto.WechatLoginBody;
import com.edudu.api.dto.UpdateUserBody;
import com.edudu.api.service.EduLoginService;
import com.edudu.common.constant.Constants;
import com.edudu.common.core.domain.AjaxResult;
import com.edudu.common.core.domain.model.EduLoginUser;
import com.edudu.common.core.domain.entity.EduUser;
import com.edudu.common.utils.SecurityUtils;
import com.edudu.common.utils.StringUtils;
import com.edudu.common.core.redis.RedisCache;

/**
 * API用户登录控制器
 * 
 * @author edudu
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "API用户登录接口")
public class EduLoginController {

    @Autowired
    private EduLoginService eduLoginService;

    @Autowired
    private RedisCache redisCache;

    private static final String SMS_CODE_KEY = "sms:code:";
    private static final long SMS_EXPIRE_TIME = 5; // 5分钟

    /**
     * 密码登录
     */
    @PostMapping("/login")
    @Operation(summary = "密码登录", description = "使用手机号和密码登录")
    public AjaxResult login(@RequestBody EduLoginBody loginBody) {
        String token = eduLoginService.login(loginBody.getPhone(), loginBody.getPassword());
        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    @Operation(summary = "注册", description = "注册新用户账号")
    public AjaxResult register(@RequestBody EduRegisterBody registerBody) {
        String token = eduLoginService.register(registerBody);
        AjaxResult ajax = AjaxResult.success("注册成功");
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/sms/send")
    @Operation(summary = "发送验证码", description = "向手机号发送登录验证码")
    public AjaxResult sendSmsCode(@RequestBody @Parameter(description = "手机号") java.util.Map<String, String> params) {
        String phone = params.get("phone");
        String type = params.getOrDefault("type", "login");

        if (StringUtils.isEmpty(phone)) {
            return AjaxResult.error("手机号不能为空");
        }

        // 生成6位验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

        // TODO: 实际应调用短信服务商API发送验证码
        // 这里模拟：存储到Redis，验证码为 "888888"（方便测试）
        String cacheKey = SMS_CODE_KEY + phone;
        if ("register".equals(type)) {
            redisCache.setCacheObject(cacheKey + ":register", code, SMS_EXPIRE_TIME, java.util.concurrent.TimeUnit.MINUTES);
        } else {
            redisCache.setCacheObject(cacheKey, code, SMS_EXPIRE_TIME, java.util.concurrent.TimeUnit.MINUTES);
        }

        // 模拟发送成功
        return AjaxResult.success("验证码发送成功");
    }

    /**
     * 短信登录
     */
    @PostMapping("/login/sms")
    @Operation(summary = "短信登录", description = "使用手机号和验证码登录")
    public AjaxResult loginBySms(@RequestBody SmsLoginBody loginBody) {
        String phone = loginBody.getPhone();
        String code = loginBody.getCode();

        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            return AjaxResult.error("手机号和验证码不能为空");
        }

        // 验证验证码
        String cacheKey = SMS_CODE_KEY + phone;
        String cachedCode = redisCache.getCacheObject(cacheKey);
        if (cachedCode == null) {
            return AjaxResult.error("验证码已过期");
        }
        if (!cachedCode.equals(code)) {
            return AjaxResult.error("验证码错误");
        }

        // 删除已使用的验证码
        redisCache.deleteObject(cacheKey);

        // 执行登录
        String token = eduLoginService.loginBySms(phone);
        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 微信登录
     */
    @PostMapping("/login/wechat")
    @Operation(summary = "微信登录", description = "使用微信授权code登录")
    public AjaxResult loginByWechat(@RequestBody WechatLoginBody loginBody) {
        // TODO: 实际应调用微信API通过code获取openid
        // 这里简化处理：直接使用code作为openid
        String token = eduLoginService.loginByWechat(loginBody.getCode(), loginBody.getNickname(), loginBody.getAvatar());
        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public AjaxResult logout() {
        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser != null) {
            String token = loginUser.getToken();
            // 删除Redis中的token
            redisCache.deleteObject(Constants.EDU_LOGIN_TOKEN_KEY + token);
        }
        return AjaxResult.success("退出成功");
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public AjaxResult getInfo() {
        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser != null) {
            EduUser user = loginUser.getUser();
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

    /**
     * 更新用户信息
     */
    @PostMapping("/user/update")
    @Operation(summary = "更新用户信息")
    public AjaxResult updateUserInfo(@RequestBody UpdateUserBody body) {
        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("未登录");
        }

        int result = eduLoginService.updateUserInfo(loginUser.getUserId(), body);
        if (result > 0) {
            return AjaxResult.success("更新成功");
        }
        return AjaxResult.error("更新失败");
    }
}
