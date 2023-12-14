package com.rpay.service.query;

import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.model.BankDetail;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Email;
import java.util.Date;

/**
 * 查询银行对象
 * @author steven
 */
@Data
public class BankQuery extends Page<BankDetail> {
    @ApiParam(name = "userId", value = "用户id，不需要输入", required = false)
    private Long userId ;
    @ApiParam(name = "bankStatus", value = "审核状态 0未审核，1已通过，2驳回", required = false)
    private Integer bankStatus ;
    @ApiParam(name = "email", value = "后台管理才需要可以输入的字段，根据email可以搜索指定用户的银行账户", required = false)
    @Email
    private String email ;
    @ApiParam(name = "swiftCode", value = "搜索指定swift code的银行账户", required = false)
    private String swiftCode ;
    @ApiParam(name = "bankName", value = "银行名称，模糊匹配", required = false)
    private String bankName ;
    @ApiParam(name = "bankAccount", value = "银行账户", required = false)
    private String bankAccount ;
}
