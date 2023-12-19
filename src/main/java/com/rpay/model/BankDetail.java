package com.rpay.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

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
    @NotBlank(message = "账户名称不能为空")
    @Size(min = 4, max = 32,message = "账户名称请输入[4 - 32]之间的中文字或者英文字母")
    private String accountName ;

    /**
     * 户主所述国家
     */
    @ApiParam(name = "country", value = "账户持有人所属国家", required = true)
    @NotBlank(message = "账户持有人所属国家必选")
    @Size(min = 2, max = 8,message = "请选择正确的账户持有人国家")
    private String country ;

    /**
     * 户主居住地址
     */
    @ApiParam(name = "accountAdd", value = "账户持有人居住地址", required = true)
    @NotBlank(message = "账户持有人地址不能为空")
    @Size(min = 6, max = 128,message = "账户持有人地址不能超过128个英文长度")
    private String accountAdd ;

    /**
     * 银行名称
     */
    @ApiParam(name = "bankName", value = "银行名称", required = true)
    @NotBlank(message = "银行名称不能为空")
    @Size(min = 3, max = 64,message = "账户持有人地址不能超过64个字节长度")
    private String bankName ;

    /**
     * swift代码
     */
    @ApiParam(name = "swiftCode", value = "swift代码", required = true)
    @NotBlank(message = "swift不能为空")
    @Size(min = 4, max = 16, message = "请输入正确的swift")
    private String swiftCode ;

    /**
     * 银行代码
     */
    @ApiParam(name = "bankCode", value = "银行代码", required = true)
    //@Size(min = 2, max = 16, message = "请输入正确的银行代码")
    private String bankCode ;

    /**
     * 银行账号
     */
    @ApiParam(name = "bankAccount", value = "银行账户号码", required = true)
    @NotBlank(message = "账户号码不能为空")
    @Size(min = 6, max = 32, message = "请输入正确的账户号码")
    private String bankAccount ;

    /**
     * 开户国家
     */
    @ApiParam(name = "bankCountry", value = "开户行所属国家", required = true)
    @NotBlank(message = "开户银行所属国家必选")
    @Size(min = 2, max = 8,message = "请选择正确的开户银行所属国家")
    private String bankCountry ;

    /**
     * 开户行地址
     */
    @ApiParam(name = "bankAdd", value = "开户银行地址", required = true)
    @NotBlank(message = "开户银行地址不能为空")
    @Size(min = 6, max = 128,message = "开户银行地址不能超过128个英文长度")
    private String bankAdd ;

    /**
     * 证明材料，银行流水单据，银行月结单，户主护照等
     */
    @ApiParam(name = "accountCer", value = "证明材料，银行流水单据，银行月结单，户主护照等，支持PDF或压缩文件(zip,rar),20M以内", required = true)
    @NotBlank(message = "企业证明材料必须上传，请检查是否上传资料")
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
