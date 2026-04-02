package com.edudu.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.edudu.common.core.controller.BaseController;
import com.edudu.common.core.domain.AjaxResult;
import com.edudu.common.core.domain.model.RegisterBody;
import com.edudu.common.utils.StringUtils;
import com.edudu.framework.web.service.SysRegisterService;
import com.edudu.system.service.ISysConfigService;

/**
 * 注册验证
 * 
 * @author edudu
 */
@RestController
public class SysRegisterController extends BaseController
{
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
