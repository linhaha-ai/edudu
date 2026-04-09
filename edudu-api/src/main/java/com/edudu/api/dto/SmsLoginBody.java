package com.edudu.api.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

/**
 * 短信登录请求
 */
@Data
public class SmsLoginBody {

    @Parameter(description = "手机号")
    private String phone;

    @Parameter(description = "验证码")
    private String code;
}
