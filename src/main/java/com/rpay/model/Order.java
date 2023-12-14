package com.rpay.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author steven
 */
@Data
@TableName("trade_order")
@EqualsAndHashCode(callSuper = true)
public class Order extends Model<Order> {
    /**
     * 自增id
     */
    @TableId
    private Long id;
    private String providerId ;
    private String name ;
    private Integer side ;
    private Integer market ;
    private Double quantity ;
    private Double price ;
    private Double tradeValue ;
    private Double espPrice ;
    private Double profit ;
    private Double myProfit ;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifiedTime;

    @TableField(exist = false)
    private Long userId ;
}
