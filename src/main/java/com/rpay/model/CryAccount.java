package com.rpay.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.rpay.model.validate.group.AdminGroup;
import com.rpay.model.validate.group.UserGroup;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 优账户信息
 * @author steven
 */
@Data
@TableName("cry_account")
@EqualsAndHashCode(callSuper = true)
public class CryAccount extends Model<CryAccount> {
    /**
     * 自增id
     */
    @TableId
    private Long id;
    @ApiParam(name = "cryCode", value = "加密货币的code，在/api/cryptocurrencies接口中获取", required = true)
    @NotBlank( message = "请选择加密货币",groups = {AdminGroup.class,UserGroup.class})
    private String cryCode ;
    @ApiParam(name = "cryName", value = "别名，用户输入时必填", required = false)
    private String cryName ;
    @ApiParam(name = "agreement", value = "加密链，目前只支持ERC或者TRC", required = true)
    @NotBlank( message = "请确认是ERC还是TRC",groups = {UserGroup.class})
    private String agreement ;
    @ApiParam(name = "cryAdd", value = "钱包地址", required = true)
    @NotBlank( message = "钱包地址必填",groups = {UserGroup.class})
    private String cryAdd ;
    @ApiParam(name = "userId", value = "添加给哪个用户", required = true)
    @NotNull( message = "请选择添加的用户",groups = {AdminGroup.class})
    private Long userId ;
    private Integer cryType ;
    @ApiParam(name = "accStatus", value = "钱包状态 1 使用中 2 失效", required = false)
    private Integer accStatus ;
    @TableField(exist = false)
    private String trcAdd ;
    @TableField(exist = false)
    private String ercAdd ;

    @TableField(exist = false)
    @ApiParam(name = "payPass", value = "支付密码，提款时这个属性与邮箱、手机验证码必须选择一个填写", required = false)
    private String payPass ;

    @TableField(exist = false)
    @ApiParam(name = "verCode", value = "验证码，邮箱或者手机验证码", required = false)
    private String verCode ;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifiedTime;
}
