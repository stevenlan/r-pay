package com.rpay.model.bill;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 账户余额详情，涉及到精确数据操作，不能直接set，只能通过+或者-进行每一笔的计算来变更
 * @author steven
 */
@Data
@TableName("balance_detail")
@EqualsAndHashCode(callSuper = true)
public class BalanceDetail extends Model<BalanceDetail> {
    @TableId
    private Long id ;
    /**
     * 货币代码
     */
    @ApiParam(name = "id", value = "", required = false)
    private String coinCode ;
    /**
     * 账户余额，涉及到精确数据操作，不能直接set，只能通过+或者-进行每一笔的计算来变更
     */
    @ApiParam(name = "id", value = "", required = false)
    private Double balance ;
    /**
     * 账本类型，1 法币 2 加密
     */
    private Integer balType ;
    /**
     * 所属用户
     */
    @ApiParam(name = "id", value = "", required = false)
    private Long userId ;
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
