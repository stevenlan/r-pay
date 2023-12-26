package com.rpay.model.deposit;

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
 * 入金申请，入金申请流程包含提交申请，查看入金账户，上传入金凭证，财务审核，确认入金完成几个步骤，每个步骤为一个状态代码
 * @author steven
 */
@Data
@TableName("dep_request")
@EqualsAndHashCode(callSuper = true)
public class DepRequest extends Model<DepRequest> {
    @TableId
    @ApiParam(name = "id", value = "", required = false)
    private Long id ;
    /**
     * 入金货币
     */
    @ApiParam(name = "coinCode", value = "法币入金货币代码，必选，只能选择法币货币代码", required = true)
    @NotBlank(message = "{deposit.coinCode.empty}")
    private String coinCode ;
    /**
     * 入金收款银行账户
     */
    @ApiParam(name = "bankId", value = "入金时用于收款的银行账户，无需填写，只需要传递coinCode查询即刻获得管理端设置好对应的入金收款账户", required = true)
    @NotNull(message = "{deposit.bank.empty}")
    private Long bankId ;
    /**
     * 入金打款银行账户
     */
    @ApiParam(name = "sendBank", value = "入金时用户的打款账户，必选自己登记过的账户", required = true)
    @NotNull(message = "{deposit.sendBank.empty}")
    private Long sendBank ;
    /**
     * 入金金额
     */
    @ApiParam(name = "reqValue", value = "申请入金金额", required = true)
    @NotNull(message = "{deposit.reqValue.empty}")
    private Double reqValue ;
    @ApiParam(name = "depValue", value = "实际到账金额，管理端审核时需要填写", required = false)
    private Double depValue ;
    /**
     * 入金用户
     */
    @ApiParam(name = "userId", value = "申请用户，无需填写", required = false)
    private Long userId ;
    /**
     * 核算审核人
     */
    @ApiParam(name = "recorded", value = "审核人，无需填写", required = false)
    private Long recorded ;
    /**
     * 申请状态 1 提交申请 2 确认汇款 3 财务审核 4 入账完成
     */
    @ApiParam(name = "reqStatus", value = "申请状态 1 提交申请 2 确认汇款 3 财务审核 4 入账完成 5拒绝入账", required = false)
    private Integer reqStatus ;
    /**
     * 入金凭证上传文件(转账流水)，在确认汇款时提交
     */
    @ApiParam(name = "reqProof", value = "入金汇款凭证，提交申请完成后，第二步需要提交汇款水单截图，第二步时必填", required = false)
    private String reqProof ;
    /**
     * 备注
     */
    @ApiParam(name = "memo", value = "审批备注，驳回时可以添加备注，用户打开详情可以查看备注留言", required = false)
    private String memo ;

    @ApiParam(name = "accountName", value = "收款账户名称", required = false)
    private String accountName ;

    @ApiParam(name = "sendAccountName", value = "打款银行账户名称", required = false)
    private String sendAccount ;
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
