package com.rpay.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 收款账户
 * @author steven
 */
@Data
@TableName("sys_deposit")
@EqualsAndHashCode(callSuper = true)
public class Deposit extends Model<Deposit> {
    /**
     * 自增id
     */
    @TableId
    private Long id;
    private String coinCode ;
    private Long bankId ;
    private Integer depositStatus ;
    @TableField(exist = false)
    private BankDetail bank ;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifiedTime;
}
