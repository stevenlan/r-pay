package com.rpay.service.query;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author steven
 */
@Data
public class ExQuery {
    @ApiParam(name = "exFrom", value = "出金来源货币代码，提交出金申请时必选", required = false)
    private String exFrom ;
    @ApiParam(name = "exTarget", value = "出金目标货币代码，提交出金申请时必选", required = false)
    private String exTarget ;
    @ApiParam(name = "exValue", value = "出金金额，提交出金申请时必填", required = false)
    private Double exValue ;
    private Float beRate ;
    private Float endRate ;
    @ApiParam(name = "targetValue", value = "兑换后金额", required = false)
    private Double targetValue ;
}
