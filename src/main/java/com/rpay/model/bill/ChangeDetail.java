package com.rpay.model.bill;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 汇兑详情
 * @author steven
 */
@Data
@TableName("change_detail")
@EqualsAndHashCode(callSuper = true)
public class ChangeDetail extends Model<ChangeDetail> {
    @TableId
    private Long id;
    @ApiParam(name = "depCoin", value = "兑换来源货币", required = true)
    @NotBlank(message = "{change.depCoin.empty}")
    private String depCoin ;
    @ApiParam(name = "targetCoin", value = "兑换目标货币", required = true)
    @NotBlank(message = "{change.targetCoin.empty}")
    private String targetCoin ;
    @ApiParam(name = "depValue", value = "兑换金额", required = true)
    @NotNull(message = "{change.depValue.empty}")
    private Double depValue ;
    @ApiParam(name = "targetValue", value = "目标金额，无需传递，通过后台计算", required = false)
    private Double targetValue ;
    @ApiParam(name = "changeRate", value = "兑换汇率，无需填写，通过后台计算", required = false)
    private Float changeRate;
    /**
     * 兑换差额
     */
    @ApiParam(name = "balance", value = "兑换差额，无需填写", required = false)
    private Float balance ;
    @ApiParam(name = "userId", value = "申请人id，无需填写", required = false)
    private Long userId ;
    /**
     * 核算审核人
     */
    @ApiParam(name = "recorded", value = "审批用户，无需填写", required = false)
    private Long recorded ;
    @ApiParam(name = "changeStatus", value = "状态 0 未确认 1已确认, 无需前端提交", required = false)
    private Integer changeStatus ;
    @ApiParam(name = "outId", value = "外联业务id，无需填写", required = false)
    private Long outId ;
    @ApiParam(name = "memo", value = "备注，后台审批拒绝时需要填写原因", required = false)
    private String memo ;
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
