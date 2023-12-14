package com.rpay.service.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;

/**
 * @author steven
 */
@Data
public class DepositQuery<T> extends Page<T> {
    @ApiParam(name = "userId", value = "无需填写", required = false)
    private Long userId ;
    @ApiParam(name = "email", value = "根据注册邮箱匹配用户的所有入金申请", required = false)
    private String email ;
    @ApiParam(name = "coinCode", value = "根据货币代码匹配所有入金申请，只能选择法币", required = false)
    private String coinCode ;
    @ApiParam(name = "startTime", value = "大于该时间点的所有入金申请", required = false)
    private Date startTime ;
    @ApiParam(name = "endTime", value = "小于该时间点的所有入金申请", required = false)
    private Date endTime ;
    @ApiParam(name = "status", value = "可以查询四种状态的出金申请 1 提交申请 2 确认汇款 3 入账完成", required = false)
    private Integer status ;
}
