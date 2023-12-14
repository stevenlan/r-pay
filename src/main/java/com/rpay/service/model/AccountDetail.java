package com.rpay.service.model;

import com.rpay.model.KycCertification;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * 账号信息详情
 * @author steven
 */
@Data
public class AccountDetail {
    /**
     * 认证信息
     */
    @ApiParam(name = "kyc", value = "认证信息详情", required = false)
    private KycCertification kyc;

    /**
     * 银行账户列表
     */
    @ApiParam(name = "banks", value = "已创建银行账户数量", required = false)
    private int banks ;

    /**
     * 银行账户列表
     */
    @ApiParam(name = "authBanks", value = "已审核通过银行账户数量", required = false)
    private int authBanks ;
}
