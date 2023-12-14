package com.rpay.model.bill;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 账单详情(出，入)
 * @author steven
 */
@Data
@TableName("bill_detail")
@EqualsAndHashCode(callSuper = true)
public class BillDetail extends Model<BillDetail> {
    @TableId
    private Long id ;
    /**
     * 账单类型 1 入账，2 出账
     */
    @ApiParam(name = "billType", value = "", required = false)
    private Integer billType ;
    /**
     * 账单货币
     */
    @ApiParam(name = "coinCode", value = "", required = false)
    private String coinCode ;
    /**
     * 账单金额
     */
    @ApiParam(name = "billValue", value = "", required = false)
    private Double billValue ;
    /**
     * 手续费
     */
    @ApiParam(name = "commission", value = "", required = false)
    private Float commission ;

    @ApiParam(name = "balance", value = "", required = false)
    private Double balance ;

    /**
     * 操作用户
     */
    @ApiParam(name = "userId", value = "", required = false)
    private Long userId ;
    /**
     * 核算审核人
     */
    @ApiParam(name = "recorded", value = "", required = false)
    private Long recorded ;

    /**
     * 关联业务类型
     */
    @ApiParam(name = "outerType", value = "", required = false)
    private Integer outerType ;
    /**
     * 关联业务id
     */
    @ApiParam(name = "outerId", value = "", required = false)
    private Long outerId ;

    @TableField(exist = false)
    private boolean isCry ;
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
