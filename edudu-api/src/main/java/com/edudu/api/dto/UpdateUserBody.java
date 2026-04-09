package com.edudu.api.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import java.util.Date;

/**
 * 更新用户信息请求
 */
@Data
public class UpdateUserBody {

    @Parameter(description = "昵称")
    private String nickname;

    @Parameter(description = "头像URL")
    private String avatar;

    @Parameter(description = "性别(0=未知,1=男,2=女)")
    private String gender;

    @Parameter(description = "生日")
    private Date birthday;
}
