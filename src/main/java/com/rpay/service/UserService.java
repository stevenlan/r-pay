package com.rpay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rpay.model.User;

/**
 * 用户表业务接口
 *
 * @author dinghao
 * @date 2021/3/16
 */
public interface UserService extends IService<User> {

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    boolean addUser(User user);

    /**
     * 检测并激活用户
     * @param active
     * @return
     */
    boolean checkUserActive(String active) ;

    /**
     * 判断用户是否管理员
     * @param user 当前登录的用户信息
     * @return
     */
    boolean isAdmin(User user) ;

    /**
     * 发送邮件给指定的用户
     * @param context 发送内容
     * @param mail 目标邮箱
     * @param title 标题
     */
    void sendMail(String title ,String context, String mail) ;

    /**
     * 生成验证码并发送邮件
     * @param mail 邮箱地址
     * @param code 验证码
     */
    void mailCheckCode(String mail, String code) ;

    /**
     * 忘记密码后，根据邮箱重置密码
     * @param mail 需要重置的用户邮箱
     * @param pass 新的密码
     */
    void resetPass(String mail, String pass) ;

    /**
     * 用户主动设置新密码
     * @param userId 用户id
     * @param oldPass 旧密码
     * @param pass 新密码
     */
    void resetPass(Long userId, String oldPass, String pass) ;

    /**
     * 设置支付密码
     * @param userId 用户id
     * @param oldPass 旧密码
     * @param pass 新密码
     */
    void setPayPass(Long userId, String oldPass, String pass) ;

    /**
     * 激活用户状态
     * @param userId 用户id
     */
    void activeUser(Long userId) ;

    /**
     * 校验支付密码
     * @param userId 用户id
     * @param payPass 支付密码
     * @return
     */
    boolean checkPayPass(Long userId, String payPass) ;

    /**
     * 分页查询指定用户的推荐列表
     * @param code
     * @param query
     * @return
     */
    Page<User> inviteUser(String code, Page<User> query) ;
}
