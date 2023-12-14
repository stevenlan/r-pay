package com.rpay.controller.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author steven
 */
@Data
public class PerVO {
    @ApiParam(name = "id", value = "审批的id", required = true)
    @NotNull( message = "审批ID不能为空")
    //@Pattern(regexp="^\\d+$", message = "审批ID必须是正整数")
    private Long id ;
    @ApiParam(name = "pass", value = "审批结果，boolean值，true通过，false拒绝", required = true)
    @NotNull(message = "审批结果必选")
    private Boolean pass ;
    @ApiParam(name = "reason", value = "拒绝说明", required = false)
    private String reason ;
}
