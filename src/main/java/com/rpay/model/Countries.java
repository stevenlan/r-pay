package com.rpay.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 银行账户信息
 * @author steven
 */
@Data
@TableName("sys_countries")
@EqualsAndHashCode(callSuper = true)
public class Countries extends Model<Countries> {
    /**
     * 自增id
     */
    @TableId
    private Long id;
    private String name ;
    private String code ;
    private String enName ;
    private String areaCode ;
    private String coinCode ;
    private Integer coinType ;
}
