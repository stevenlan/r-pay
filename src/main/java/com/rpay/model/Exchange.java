package com.rpay.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 银行账户信息
 * @author steven
 */
@Data
@TableName("sys_exchange")
@EqualsAndHashCode(callSuper = true)
public class Exchange extends Model<Exchange> {
    /**
     * 自增id
     */
    @TableId
    private Long id;
    @ApiParam(name = "fromCountry", value = "来源货币国家，可以是法币，也可以是数据货币,提交的是地区列表的code", required = true)
    @NotBlank( message = "来源货币必选")
    private String fromCountry ;
    @ApiParam(name = "exFrom", value = "来源货币，可以是法币，也可以是数据货币,提交的是地区列表的coinCode", required = true)
    @NotBlank( message = "来源货币必选")
    private String exFrom ;
    @ApiParam(name = "targetCountry", value = "目标货币国家，可以是法币，也可以是数据货币,提交的是地区列表的code", required = true)
    @NotBlank( message = "来源货币必选")
    private String targetCountry ;
    @ApiParam(name = "exTarget", value = "目标货币，可以是法币，也可以是数据货币,提交的是地区列表的coinCode", required = true)
    @NotBlank( message = "目标货币必选")
    private String exTarget ;
    @ApiParam(name = "exRate", value = "兑换汇率", required = true)
    @NotNull( message = "兑换汇率必填")
    private Float exRate ;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifiedTime;
}
