package com.rpay.service.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.model.bill.ChangeDetail;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;

/**
 * @author steven
 */
@Data
public class ChangeDetailQuery extends Page<ChangeDetail> {
    @ApiParam(name = "email", value = "指定用户的查询", required = false)
    private String email ;
    @ApiParam(name = "changeStatus", value = "指定状态查询", required = false)
    private Integer changeStatus ;
    @ApiParam(name = "fromCode", value = "兑换记录查询的兑换源货币种类", required = false)
    private String fromCode;
    @ApiParam(name = "targetCode", value = "兑换记录查询的兑换目标种类", required = false)
    private String targetCode ;
    @ApiParam(name = "startTime", value = "大于该时间点的所有出金申请", required = false)
    private Date startTime ;
    @ApiParam(name = "endTime", value = "小于该时间点的所有出金申请", required = false)
    private Date endTime ;
}
