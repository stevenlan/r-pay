package com.rpay.service.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;

/**
 * @author steven
 */
@Data
public class WithdrawQuery<T> extends Page<T> {
    @ApiParam(name = "email", value = "根据注册邮箱匹配用户的所有出金申请", required = false)
    private String email ;
    @ApiParam(name = "coinCode", value = "根据货币代码匹配所有出金申请，只能选择法币", required = false)
    private String coinCode ;
    @ApiParam(name = "startTime", value = "大于该时间点的所有出金申请", required = false)
    private Date startTime ;
    @ApiParam(name = "endTime", value = "小于该时间点的所有出金申请", required = false)
    private Date endTime ;
    @ApiParam(name = "status", value = "可以查询四种状态的出金申请 1 已提交 2 申请已确认 3 出金完成 4 确认到账", required = false)
    private Integer status ;
}
