package com.rpay.model.withdraw;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 出金申请，出金账户选择，出金金额确定，财务审核，财务出账，出金确认完成
 * @author steven
 */
@Data
@TableName("withdraw_request")
@EqualsAndHashCode(callSuper = true)
public class WithdrawRequest extends Model<WithdrawRequest> {
    @TableId
    private Long id ;
    /**
     * 出金币种
     */
    @ApiParam(name = "coinCode", value = "出金货币代码，必选，只能选择法币", required = true)
    @NotBlank(message = "{withdraw.coinCode.empty}")
    private String coinCode ;

    @ApiParam(name = "targetCode", value = "出金目标货币代码，已废除", required = false)
    private String targetCode ;
    /**
     * 申请出金额度
     */
    @ApiParam(name = "reqValue", value = "申请出金金额，必填", required = true)
    @NotNull(message = "{withdraw.reqValue.empty}")
    private Double reqValue ;
    /**
     * 实际出金金额
     */
    @ApiParam(name = "changeValue", value = "经过汇率计算后预计汇出金额，不需要填写", required = false)
    private Double changeValue ;
    /**
     * 实际出金金额
     */
    @ApiParam(name = "withdrawValue", value = "实际出金金额，最后财务审核确认出账时必填", required = false)
    private Double withdrawValue ;
    /**
     * 手续费
     */
    @ApiParam(name = "commission", value = "手续费，最后财务审核确认出账时必填", required = false)
    private Float commission ;
    /**
     * 出金接收银行账户
     */
    @ApiParam(name = "bankId", value = "接款账户，必填", required = true)
    @NotNull(message = "{withdraw.bank.empty}")
    private Long bankId ;
    /**
     * 出金申请账户
     */
    @ApiParam(name = "userId", value = "申请出金用户，无需填写", required = false)
    private Long userId ;
    /**
     * 核算审核人
     */
    @ApiParam(name = "recorded", value = "操作人，无需填写", required = false)
    private Long recorded ;
    /**
     * 申请状态 1 业务审批 2 财务审批 3 出金完成 4 确认到账(超时1天自动确认) 5拒绝提款
     */
    @ApiParam(name = "reqStatus", value = "申请状态 1 业务审批 2 财务审批 3 出金完成 4 确认到账(超时1天自动确认)", required = false)
    private Integer reqStatus ;
    /**
     * 出金合同、物流凭证非必填
     */
    @ApiParam(name = "reqProof", value = "提交申请时，可以补充相应的合同、物流凭证作为说明，非必填，但建议填", required = false)
    private String reqProof ;
    /**
     * 出金凭证上传文件
     */
    @ApiParam(name = "withdrawProof", value = "财务审核出款时，必填", required = false)
    private String withdrawProof ;
    /**
     * 备注
     */
    @ApiParam(name = "memo", value = "如审核驳回，可以添加说明告诉用户驳回原因，用户可通过查看出金申请详情查看备注", required = false)
    private String memo ;

    @ApiParam(name = "accountName", value = "收款账户名称", required = false)
    private String accountName ;

    @TableField(exist = false)
    @ApiParam(name = "payPass", value = "支付密码，提款时这个属性与邮箱、手机验证码必须选择一个填写", required = false)
    private String payPass ;

    @TableField(exist = false)
    @ApiParam(name = "verCode", value = "验证码，邮箱或者手机验证码", required = false)
    private String verCode ;
    /**
     * 注册时间
     */
    @ApiParam(name = "createTime", value = "创建时间", required = false)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiParam(name = "modifiedTime", value = "修改时间", required = false)
    private Date modifiedTime;
}
