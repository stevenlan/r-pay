package com.rpay.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author steven
 */
@Data
@TableName("sys_coin_limit")
@EqualsAndHashCode(callSuper = true)
public class CoinLimit extends Model<CoinLimit> {

    /**
     * 自增id
     */
    @TableId
    @ApiParam(name = "id", value = "ID，无需填写，登录成功后返回含有该参数", required = false)
    private Long id;
    private String coinCode ;
    private Float coinMin ;
    private Double coinMax ;
    private Double commission ;
    private String optType ;

    /**
     * 注册时间
     */
    @ApiParam(name = "createTime", value = "无需填写的参数", required = false)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiParam(name = "modifiedTime", value = "无需填写的参数", required = false)
    private Date modifiedTime;
}
