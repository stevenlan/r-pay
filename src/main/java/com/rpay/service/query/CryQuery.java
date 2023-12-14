package com.rpay.service.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;

/**
 * @author steven
 */
@Data
public class CryQuery<T> extends Page<T> {
    @ApiParam(name = "type", value = "1 入金 2 出金的申请", required = false)
    private Integer type ;
    @ApiParam(name = "email", value = "根据注册邮箱匹配用户的所有申请", required = false)
    private String email ;
    @ApiParam(name = "startTime", value = "大于该时间点的所有申请", required = false)
    private Date startTime ;
    @ApiParam(name = "endTime", value = "小于该时间点的所有申请", required = false)
    private Date endTime ;
    @ApiParam(name = "status", value = "若是入金 1 已提交 2 已入账 若是出金 1 已提交 2 已出账", required = false)
    private Integer status ;
}
