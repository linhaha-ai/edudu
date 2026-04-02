package com.edudu.api.controller;

import com.edudu.edu.domain.EduUser;
import com.edudu.edu.domain.model.EduLoginBody;
import com.edudu.edu.domain.model.EduRegisterBody;
import com.edudu.edu.domain.vo.EduUserInfoVO;
import com.edudu.edu.service.IEduTokenService;
import com.edudu.edu.service.IEduUserService;
import com.edudu.common.core.domain.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 前端用户登录接口
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "前端用户认证接口")
public class EduLoginController {

    private final IEduUserService userService;
    private final IEduTokenService tokenService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "支持手机号+验证码、微信、账号密码三种登录方式")
    public AjaxResult login(@RequestBody @Valid EduLoginBody loginBody, HttpServletRequest request) {
        try {
            String token = userService.login(loginBody);
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("expireTime", 30 * 24 * 60 * 60); // 30天
            
            log.info("用户登录成功: {}", loginBody.getPhone());
            return AjaxResult.success(result);
        } catch (Exception e) {
            log.warn("用户登录失败: {}", e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public AjaxResult register(@RequestBody @Valid EduRegisterBody registerBody) {
        try {
            Long userId = userService.register(registerBody);
            
            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            
            log.info("用户注册成功: {}", registerBody.getPhone());
            return AjaxResult.success(result);
        } catch (Exception e) {
            log.warn("用户注册失败: {}", e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 发送验证码
     */
    @PostMapping("/sendCode")
    @Operation(summary = "发送验证码", description = "发送登录/注册验证码到手机")
    public AjaxResult sendCode(
            @Parameter(description = "手机号") @RequestParam String phone) {
        try {
            boolean success = userService.sendCode(phone);
            return AjaxResult.success(success ? "验证码发送成功" : "验证码发送失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/userInfo")
    @Operation(summary = "获取用户信息")
    public AjaxResult getUserInfo(HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return AjaxResult.error("请先登录");
            }
            
            EduUserInfoVO userInfo = userService.getUserInfo(userId);
            return AjaxResult.success(userInfo);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public AjaxResult logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            tokenService.deleteToken(token);
        }
        return AjaxResult.success("退出成功");
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/update")
    @Operation(summary = "修改用户信息")
    public AjaxResult update(@RequestBody EduUser user, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return AjaxResult.error("请先登录");
            }
            
            user.setUserId(userId);
            userService.updateUser(user);
            return AjaxResult.success("修改成功");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public AjaxResult updatePassword(
            @Parameter(description = "旧密码") @RequestParam(required = false) String oldPassword,
            @Parameter(description = "新密码") @RequestParam String newPassword,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return AjaxResult.error("请先登录");
            }
            
            userService.updatePassword(userId, oldPassword, newPassword);
            return AjaxResult.success("密码修改成功");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 从请求中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    /**
     * 从请求中获取用户ID
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            return tokenService.getUserIdByToken(token);
        }
        return null;
    }
}
