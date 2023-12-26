package com.rpay.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 用户表实体
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper = true)
public class User extends Model<User> {

    /**
     * 自增id
     */
    @TableId
    @ApiParam(name = "id", value = "用户ID，无需填写，登录成功后返回含有该参数", required = false)
    private Long id;

    /**
     * 用户名
     */
    @ApiParam(name = "username", value = "用户名称，暂时无需填写，注册时不显示", required = false)
    private String username;

    /**
     * 密码
     */
    @ApiParam(name = "password", value = "必填，密码，前端需要一个确认密码来保证用户两次输入都是同一个密码", required = true)
    @NotBlank(message = "{user.pwd.empty}")
    @Size(min=6, max = 16, message = "{user.pwd.size}")
    private String password;

    /**
     * 昵称
     */
    @ApiParam(name = "nickName", value = "用户昵称，暂时无需填写，注册时不显示", required = false)
    private String nickName;

    /**
     * 邮箱
     */
    @ApiParam(name = "email", value = "邮箱地址，电话和邮箱必填其一，用户登录时的账号依据之一", required = true)
    @NotBlank(message = "{user.email.empty}")
    @Email(message = "{user.email.format}")
    private String email ;
    @ApiParam(name = "phone", value = "电话好吗，电话和邮箱必填其一，用户登录时的账号依据之一", required = false)
    private String phone ;
    @ApiParam(name = "countries", value = "请选择正确的国家地区代码", required = false)
    private String countries ;
    @ApiParam(name = "areaCode", value = "地区电话号，填写电话时此项必填", required = false)
    private String areaCode ;

    /**
     * 隔离id
     */
    @ApiParam(name = "providerId", value = "无需填写的参数", required = false)
    private String providerId ;

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

    /**
     * 状态 1 正常 0 未激活
     */
    @ApiParam(name = "userStatus", value = "用户状态，无需填写提交，1 正常，0 未确认，邮箱或者手机号未经过确认码确认的均为未确认状态，无法完成注册", required = false)
    private Integer userStatus ;

    /**
     * 推广码，谁推广进来的
     */
    @ApiParam(name = "inviteCode", value = "推广码，默认推广码是SUPER-PAY", required = true)
    @NotBlank( message = "{user.inviteCode.empty}")
    private String inviteCode;

    @ApiParam(name = "payPass", value = "支付密码", required = true)
    private String payPass ;

    /**
     * 激活码
     */
    @ApiParam(name = "checkCode", value = "邮箱或者手机确认码，目前未对接接口方，统一输入8888", required = true)
    @NotBlank( message = "{user.checkCode.empty}")
    private String checkCode ;

    /**
     * 父级
     */
    @ApiParam(name = "parentId", value = "无需使用的参数", required = false)
    private Long parentId ;

    @TableField(exist = false)
    @ApiParam(name = "code", value = "图片验证码", required = true)
    private String code ;

    @TableField(exist = false)
    @ApiParam(name = "from", value = "无需使用的参数", required = false)
    private String from ;

    @TableField(exist = false)
    @ApiParam(name = "reason", value = "无需使用的参数", required = false)
    private String reason ;

    @TableField(exist = false)
    @ApiParam(name = "hasPayPass", value = "无需输入的参数，是否设置了支付密码", required = false)
    private boolean hasPayPass ;

    /**
     * 角色集合
     */
    @TableField(exist = false)
    @ApiParam(name = "roleList", value = "无需使用的参数", required = false)
    private List<String> roleList;

    /**
     * 权限集合
     */
    @TableField(exist = false)
    @ApiParam(name = "authList", value = "无需使用的参数", required = false)
    private List<String> authList;

    @TableField(exist = false)
    @ApiParam(name = "admin", value = "不需要输入，是否管理员，登录成功后判断进入管理后台还是用户后台标识", required = false)
    private boolean admin ;
}
