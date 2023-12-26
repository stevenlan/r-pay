package com.rpay.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.rpay.model.validate.group.BankNor;
import com.rpay.model.validate.group.BankSimple;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 银行账户信息
 * @author steven
 */
@Data
@TableName("bank_detail")
@EqualsAndHashCode(callSuper = true)
public class BankDetail extends Model<BankDetail> {
    /**
     * 自增id
     */
    @TableId
    @ApiParam(name = "id", value = "不需要输入，修改时需要隐含参数输入", required = false)
    private Long id;

    /**
     * 账号名称
     */
    @ApiParam(name = "accountName", value = "账户名称", required = true)
    @NotBlank(message = "{bank.accountName.empty}", groups = {BankNor.class, BankSimple.class})
    @Size(min = 4, max = 32,message = "{bank.accountName.size}", groups = {BankNor.class, BankSimple.class})
    private String accountName ;

    /**
     * 户主所述国家
     */
    @ApiParam(name = "country", value = "账户持有人所属国家", required = true)
    @NotBlank(message = "{bank.country.empty}", groups = {BankNor.class})
    @Size(min = 2, max = 8,message = "{bank.country.size}", groups = {BankNor.class})
    private String country ;

    /**
     * 户主居住地址
     */
    @ApiParam(name = "accountAdd", value = "账户持有人居住地址", required = true)
    @NotBlank(message = "{bank.accountAdd.empty}", groups = {BankNor.class})
    @Size(min = 6, max = 128,message = "{bank.accountAdd.size}", groups = {BankNor.class})
    private String accountAdd ;

    /**
     * 银行名称
     */
    @ApiParam(name = "bankName", value = "银行名称", required = true)
    @NotBlank(message = "{bank.bankName.empty}", groups = {BankNor.class, BankSimple.class})
    @Size(min = 3, max = 64,message = "{bank.bankName.size}", groups = {BankNor.class, BankSimple.class})
    private String bankName ;

    /**
     * swift代码
     */
    @ApiParam(name = "swiftCode", value = "swift代码", required = true)
    @NotBlank(message = "{bank.swiftCode.empty}", groups = {BankNor.class})
    @Size(min = 4, max = 16, message = "{bank.swiftCode.size}", groups = {BankNor.class})
    private String swiftCode ;

    /**
     * 银行代码
     */
    @ApiParam(name = "bankCode", value = "银行代码", required = true)
    private String bankCode ;

    /**
     * 银行账号
     */
    @ApiParam(name = "bankAccount", value = "银行账户号码", required = true)
    @NotBlank(message = "{bank.bankAccount.empty}", groups = {BankNor.class, BankSimple.class})
    @Size(min = 6, max = 32, message = "{bank.bankAccount.size}", groups = {BankNor.class, BankSimple.class})
    private String bankAccount ;

    /**
     * 开户国家
     */
    @ApiParam(name = "bankCountry", value = "开户行所属国家", required = true)
    @NotBlank(message = "{bank.bankCountry.empty}", groups = {BankNor.class})
    @Size(min = 2, max = 8,message = "{bank.bankCountry.size}", groups = {BankNor.class, BankSimple.class})
    private String bankCountry ;

    /**
     * 开户行地址
     */
    @ApiParam(name = "bankAdd", value = "开户银行地址", required = true)
    @NotBlank(message = "{bank.bankAdd.empty}", groups = {BankNor.class})
    @Size(min = 6, max = 128,message = "{bank.bankAdd.size}", groups = {BankNor.class})
    private String bankAdd ;

    @TableField(exist = false)
    private List<String> bindCoins ;

    /**
     * 证明材料，银行流水单据，银行月结单，户主护照等
     */
    @ApiParam(name = "accountCer", value = "证明材料，银行流水单据，银行月结单，户主护照等，支持PDF或压缩文件(zip,rar),20M以内", required = true)
    @NotBlank(message = "{bank.accountCer.empty}", groups = {BankNor.class})
    private String accountCer ;

    /**
     * 所属用户
     */
    @ApiParam(name = "userId", value = "所属用户", required = false)
    private Long userId ;

    @ApiParam(name = "bankStatus", value = "审核状态 0未审核 1 已通过 2 驳回，后台审核时必选", required = false)
    private Integer bankStatus ;

    /**
     * 审核备注
     */
    @ApiParam(name = "reason", value = "审核备注，后台审核选择驳回时必须说明原因", required = false)
    private String reason ;
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

    @TableField(exist = false)
    @ApiParam(name = "coinCode", value = "管理后台添加收款账户时需要提供的货币代码，用户提交时无需带上", required = false)
    private String coinCode ;

    @TableField(exist = false)
    @ApiParam(name = "payPass", value = "支付密码，提款时这个属性与邮箱、手机验证码必须选择一个填写", required = false)
    private String payPass ;

    @TableField(exist = false)
    @ApiParam(name = "verCode", value = "验证码，邮箱或者手机验证码", required = false)
    private String verCode ;
}
