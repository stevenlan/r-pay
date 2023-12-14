package com.rpay.model;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author steven
 */
@Data
@TableName("kyc_certification")
@EqualsAndHashCode(callSuper = true)
public class KycCertification extends Model<KycCertification> {
    /**
     * 自增id
     */
    @TableId
    @ApiParam(name = "id", value = "不需要输入，修改时需要传入", required = false)
    private Long id;

    /**
     * 企业名字
     */
    @ApiParam(name = "companyName", value = "企业名称", required = true)
    @NotBlank(message = "企业名称不能为空")
    @Size(min = 4, max = 32,message = "企业名称请输入[4 - 32]之间的中文字或者英文字母")
    private String companyName ;

    /**
     * 企业英文名字
     */
    @ApiParam(name = "companyEnName", value = "企业英文名称", required = false)
    @Size(min = 4, max = 64,message = "企业英文名称请输入[4 - 64]个英文字母")
    private String companyEnName ;

    /**
     * 经营地址
     */
    @ApiParam(name = "businessAdd", value = "企业经营地址", required = true)
    @NotBlank(message = "企业经营地址不能为空")
    @Size(min = 4, max = 128,message = "企业经营地址请输入[4 - 128]之间的中文字或者英文字母")
    private String businessAdd ;

    /**
     * 公司类型
     */
    @ApiParam(name = "busType", value = "企业类型 1 个体工商户 2 企业 3 海外公司", required = true)
    @NotNull(message = "企业类型必选")
    private Integer busType ;

    /**
     * 企业注册所在国家
     */
    @ApiParam(name = "country", value = "企业注册所在国家代码", required = true)
    @NotBlank(message = "企业注册所在国家必选")
    @Size(min = 2, max = 8,message = "请正确选择企业注册所在国家")
    private String country ;

    /**
     * 企业注册日期
     */
    @ApiParam(name = "regDate", value = "注册日期", required = true)
    @NotBlank(message = "企业注册日期必填")
    private String regDate ;

    /**
     * 企业有效期
     */
    @ApiParam(name = "period", value = "企业有效期", required = true)
    @NotBlank(message = "企业有效期必填")
    private String period ;

    /**
     * 月汇款金额
     */
    @ApiParam(name = "monthlyRemittance", value = "预估月汇款金额", required = true)
    @NotBlank(message = "预估月汇款金额必填")
    @Size(min = 2, max = 13, message = "汇款金额不能超过百亿额度，单位美金")
    private String monthlyRemittance ;

    /**
     * 月交易笔数
     */
    @ApiParam(name = "transactionsMonth", value = "预估月交易笔数", required = true)
    @NotBlank(message = "预估月交易笔数必填")
    @Size(min = 1, max = 6, message = "月交易笔数不能超过10万笔")
    private String transactionsMonth ;

    /**
     * 单笔额度
     */
    @ApiParam(name = "transactionLimit", value = "单笔交易额度，请填写单笔最大额度", required = true)
    @NotBlank(message = "单笔交易额度必填")
    @Size(min = 2, max = 9, message = "单笔交易金额不能超过亿额度，单位美金")
    private String transactionLimit ;

    /**
     * 业务场景说明
     */
    @ApiParam(name = "businessScenario", value = "业务场景说明", required = true)
    @NotBlank(message = "业务场景必填")
    @Size(min = 10, max = 256, message = "业务场景说明必须超过10个字节，不能超过256个字节")
    private String businessScenario ;

    /**
     * 企业官网
     */
    @ApiParam(name = "webSite", value = "企业官网，可空", required = false)
    private String webSite ;

    /**
     * 公司商业登记证书或营业执照，以及经营地址证明，银行水电账单等
     */
    @ApiParam(name = "regCer", value = "商业登记证书或营业执照，以及经营地址证明，银行水电账单等资料合集，支持PDF或压缩文件(zip,rar),20M以内", required = true)
    //@NotBlank(message = "企业证明资料必须上传，请检查是否已经上传")
    private String regCer ;

    /**
     * 法人护照信息
     */
    @ApiParam(name = "legal", value = "法人护照及居住地址证明，支持PDF或压缩文件(zip,rar),20M以内", required = true)
    //@NotBlank(message = "法人资料必须上传，请检查是否上传")
    private String legal ;

    /**
     * 股东结构及超过25%股份股东护照信息
     */
    @ApiParam(name = "shareholder", value = "股东结构及超过25%股份股东护照信息，非个体工商户需要提供该信息，支持PDF或压缩文件(zip,rar),20M以内", required = false)
    private String shareholder ;

    /**
     * kyc状态 0未审核 1 已通过 2 驳回
     */
    @ApiParam(name = "kycStatus", value = "后台审核时提供选择，审核状态 0未审核 1 已通过 2 驳回", required = false)
    private Integer kycStatus ;

    /**
     * 所属用户Id
     */
    @ApiParam(name = "userId", value = "无需输入，所属用户", required = false)
    private Long userId ;

    /**
     * 审核意见
     */
    @ApiParam(name = "reason", value = "审核意见，后台审核时如不通过则需要填写原因", required = false)
    private String reason ;

    /**
     * 注册时间
     */
    @ApiParam(name = "createTime", value = "无需输入，创建时间", required = false)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiParam(name = "modifiedTime", value = "无需输入，修改时间", required = false)
    private Date modifiedTime;
}
