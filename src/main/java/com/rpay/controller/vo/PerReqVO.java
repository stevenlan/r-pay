package com.rpay.controller.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author steven
 */
@Data
public class PerReqVO {
    @ApiParam(name = "id", value = "审批的id", required = true)
    @NotNull( message = "审批ID不能为空")
    private Long id ;
    @ApiParam(name = "pass", value = "审批结果，boolean值，true通过，false拒绝", required = true)
    @NotNull(message = "审批结果必选")
    private Boolean pass ;
    @ApiParam(name = "depValue", value = "入金审核时，需要填写实际到账金额", required = false)
    private Double depValue ;
    @ApiParam(name = "withdrawValue", value = "如果是状态位2的出金审核时，需要填写实际汇款金额", required = false)
    private Double withdrawValue ;
    @ApiParam(name = "commission", value = "如果是状态位2的出金审核时，需要填写手续费", required = false)
    private Float commission ;
    @ApiParam(name = "proof", value = "如果是状态位2的出金审核时，需要上传出款转账水单", required = false)
    private String proof ;
    @ApiParam(name = "proof", value = "如果是状态位驳回，则可以填写驳回原因", required = false)
    private String memo ;
    @ApiParam(name = "tid", value = "数字申请提款时，审核完成需要回填一个tid作为凭证", required = false)
    private String tid ;
}
