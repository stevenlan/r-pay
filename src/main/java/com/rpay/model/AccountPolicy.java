package com.rpay.model;

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
@TableName("account_policy")
@EqualsAndHashCode(callSuper = true)
public class AccountPolicy extends Model<AccountPolicy> {
    @TableId
    private Long id ;
    private String providerId ;
    private Double espPrice ;
    private Double minValue ;
    private Date createTime ;
    private Date modifiedTime ;
}
