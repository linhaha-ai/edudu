package com.edudu.api.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

/**
 * 微信登录请求
 */
@Data
public class WechatLoginBody {

    @Parameter(description = "微信授权code")
    private String code;

    @Parameter(description = "昵称")
    private String nickname;

    @Parameter(description = "头像URL")
    private String avatar;
}
