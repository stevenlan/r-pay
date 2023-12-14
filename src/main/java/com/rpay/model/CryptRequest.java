package com.rpay.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author steven
 */
@Data
@TableName("crypt_req")
@EqualsAndHashCode(callSuper = true)
public class CryptRequest extends Model<CryptRequest> {
    @TableId
    private Long id ;
    @ApiParam(name = "coinCode", value = "货币代码，必须选择一个加密货币代码(目前只有USDT)", required = true)
    @NotBlank
    private String coinCode ;
    @ApiParam(name = "srcCode", value = "提款来源账本的货币代码，提款时必选，充值数字货币时不需要选择", required = false)
    private String srcCode ;
    @ApiParam(name = "reqValue", value = "申请金额，必填", required = true)
    @NotNull
    private Double reqValue ;
    @ApiParam(name = "witValue", value = "兑换完成的实际金额，无需填写，后台根据汇率计算", required = false)
    private Double witValue ;
    @ApiParam(name = "agreement", value = "加密链，必填", required = true)
    @NotBlank
    private String agreement ;
    @ApiParam(name = "cryptAdd", value = "钱包地址，入金时填的是转账钱包地址，出金时填的是接收钱包地址", required = true)
    @NotBlank
    private String cryptAdd ;
    @ApiParam(name = "tid", value = "转账交易地址，入金时提交申请时填写，出金时审核出账时填写", required = false)
    private String tid ;
    /**
     * 申请类型 1 入金 2 出金
     */
    @ApiParam(name = "reqType", value = "申请类型 1 入金 2 出金，提交申请时无需填写，不同接口内确定申请的类型", required = false)
    private Integer reqType ;
    @ApiParam(name = "reqStatus", value = "若是入金 1 已提交 2 已入账 3 驳回 若是出金 1 已提交 2 已出账 3 驳回", required = false)
    private Integer reqStatus ;
    /**
     * 凭证上传文件
     */
    @ApiParam(name = "reqProof", value = "数字货币申请时的转账凭证，若是出金，则是审批时可以上传的转账截图，可以传jpg,pdf", required = false)
    private String reqProof ;
    @ApiParam(name = "userId", value = "申请用户，无需填写", required = false)
    private Long userId ;
    /**
     * 核算审核人
     */
    @ApiParam(name = "recorded", value = "审批用户，无需填写", required = false)
    private Long recorded ;
    @ApiParam(name = "memo", value = "如果驳回，可以在备注字段填写原因，用户查看详情是可以查看到", required = false)
    private String memo ;

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
