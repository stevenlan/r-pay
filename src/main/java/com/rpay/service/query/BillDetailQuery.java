package com.rpay.service.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.model.bill.BillDetail;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;

/**
 * @author steven
 */
@Data
public class BillDetailQuery extends Page<BillDetail> {
    @ApiParam(name = "email", value = "指定用户的查询", required = false)
    private String email ;
    @ApiParam(name = "bizType", value = "单据业务类型 1 法币业务 2加密货币业务", required = false)
    private Integer bizType ;
    @ApiParam(name = "coinCode", value = "记账货币种类", required = false)
    private String coinCode ;
    @ApiParam(name = "startTime", value = "大于该时间点的所有出金申请", required = false)
    private Date startTime ;
    @ApiParam(name = "endTime", value = "小于该时间点的所有出金申请", required = false)
    private Date endTime ;
}
