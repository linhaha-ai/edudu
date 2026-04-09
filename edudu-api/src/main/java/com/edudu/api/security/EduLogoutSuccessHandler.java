package com.edudu.api.security;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import com.alibaba.fastjson2.JSON;
import com.edudu.api.service.EduTokenService;
import com.edudu.common.core.domain.AjaxResult;
import com.edudu.common.utils.ServletUtils;

/**
 * API退出处理类
 * 
 * @author edudu
 */
@Configuration
public class EduLogoutSuccessHandler implements LogoutSuccessHandler
{
    @Autowired
    private EduTokenService eduTokenService;

    @Override
    public void onLogoutSuccess(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        com.edudu.common.core.domain.model.EduLoginUser loginUser = eduTokenService.getLoginUser(request);
        if (loginUser != null)
        {
            // 删除用户缓存记录
            eduTokenService.delLoginUser(loginUser.getToken());
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success("退出成功")));
    }
}
