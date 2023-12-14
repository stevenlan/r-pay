package com.rpay.service.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.model.KycCertification;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Email;
import java.util.Date;

/**
 * 查询服务对象
 * @author steven
 */
@Data
public class KycQuery extends Page<KycCertification> {
    @ApiParam(name = "userId", value = "用户id，不需要输入", required = false)
    private Long userId ;
    @ApiParam(name = "kycStatus", value = "审核状态 0未审核，1已通过，2驳回", required = false)
    private Integer kycStatus ;
    @ApiParam(name = "email", value = "邮箱", required = false)
    @Email
    private String email ;
    @ApiParam(name = "phone", value = "电话", required = false)
    private String phone ;
    @ApiParam(name = "name", value = "公司名称，模糊匹配", required = false)
    private String name ;
}
