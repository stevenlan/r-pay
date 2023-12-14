package com.rpay.controller.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author steven
 */
@Data
public class LoginVO {
    @NotBlank(message = "邮箱或者手机号不能为空")
    @ApiParam(name = "username", value = "邮箱或者手机号", required = true)
    private String username ;
    @NotBlank(message = "密码不能为空")
    @ApiParam(name = "password", value = "密码", required = true)
    private String password ;
    @NotBlank(message = "验证码不能为空")
    @ApiParam(name = "code", value = "验证码", required = false)
    private String code ;
    @ApiParam(name = "rememberMe", value = "是否记住登录状态", required = false)
    private Boolean rememberMe ;
}
