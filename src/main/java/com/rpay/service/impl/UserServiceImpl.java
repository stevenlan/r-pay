package com.rpay.service.impl;

import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rpay.common.constant.CommonConstant;
import com.rpay.common.exception.BusinessException;
import com.rpay.common.shiro.EndecryptUtil;
import com.rpay.common.utils.BizCodeUtils;
import com.rpay.mapper.RoleMapper;
import com.rpay.mapper.UserMapper;
import com.rpay.mapper.UserRoleMapper;
import com.rpay.model.Role;
import com.rpay.model.User;
import com.rpay.model.UserRole;
import com.rpay.service.UserService;
import com.rpay.service.sms.MessageService;
import com.wf.captcha.SpecCaptcha;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户业务接口实现
 *
 * @author dinghao
 * @date 2021/3/16
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserRoleMapper userRoleMapper;

    private final RoleMapper roleMapper;

    private final MessageService messageService ;

    @Override
    public boolean addUser(User user) {
        if ( baseMapper.selectCount((new QueryWrapper<User>().lambda().eq(User::getEmail, user.getEmail()))) > 0 ) {
            throw new BusinessException("邮箱已存在") ;
        }
        if ( baseMapper.selectCount((new QueryWrapper<User>().lambda().eq(User::getPhone, user.getPhone()).eq(User::getAreaCode,user.getAreaCode()))) > 0 ) {
            throw new BusinessException("手机号码已存在") ;
        }
        if ( baseMapper.selectCount((new QueryWrapper<User>().lambda().eq(User::getProviderId, user.getInviteCode()))) != 1 ) {
            throw new BusinessException("推荐人不存在") ;
        }
        user.setPassword(EndecryptUtil.encrytMd5(user.getPassword(), CommonConstant.DEFAULT_SALT, CommonConstant.DEFAULT_HASH_COUNT));
        user.setProviderId(BizCodeUtils.romUniqueId());
        if (baseMapper.insert(user) <= 0) {
            throw new BusinessException("用户新增失败");
        }
        //给用户设置基本角色
        UserRole ur = new UserRole();
        ur.setUserId(user.getId());
        ur.setRoleId(roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleCode, CommonConstant.ROLE_USER)).getId());
        if (userRoleMapper.insert(ur) <= 0) {
            throw new BusinessException("用户新增失败");
        }
        return true;
    }

    @Override
    public boolean checkUserActive(String active) {
        User user = baseMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getCheckCode, active)) ;
        if ( null != user && user.getUserStatus() != 1 ) {
            user.setUserStatus(1);
            user.setCheckCode(null);
            baseMapper.updateById(user) ;
            return true ;
        }
        return false ;
    }

    @Override
    public boolean isAdmin(User user) {
        List<String> roleList = userRoleMapper.selectRoleCodesById(user.getId());
        Set<String> roleSet = new HashSet<>(roleList);
        return roleSet.contains("admin") ;
    }

    @Override
    public void mailCheckCode(String mail, String code) {
        messageService.sendMail("感谢您的使用ReliancePay，请查看邮箱验证码",code,"30分钟", mail) ;
    }

    @Override
    public void resetPass(String mail, String pass) {
        User user = this.getOne(new QueryWrapper<User>().lambda().eq(User::getEmail,mail)) ;
        if ( null == user ) {
            user = this.getOne(new QueryWrapper<User>().lambda().eq(User::getPhone,mail)) ;
            if ( null == user ) {
                throw new BusinessException("该用户不存在") ;
            }
        }
        //不能用相同的密码
        //给新密码加密
        String cryPass = EndecryptUtil.encrytMd5(pass, CommonConstant.DEFAULT_SALT, CommonConstant.DEFAULT_HASH_COUNT) ;
        if (StringUtils.equals(cryPass,user.getPassword())) {
            throw new BusinessException("不能设置为相同的密码") ;
        }

        resetPass(user.getId(), cryPass) ;
    }

    @Override
    public void resetPass(Long userId, String oldPass, String pass) {
        User user = this.getById(userId) ;
        //加密对比
        String oldCry = EndecryptUtil.encrytMd5(oldPass, CommonConstant.DEFAULT_SALT, CommonConstant.DEFAULT_HASH_COUNT) ;
        if ( !StringUtils.equals(oldCry,user.getPassword()) ) {
            throw new BusinessException("旧密码错误！") ;
        }
        //给新密码加密
        String cryPass = EndecryptUtil.encrytMd5(pass, CommonConstant.DEFAULT_SALT, CommonConstant.DEFAULT_HASH_COUNT) ;
        resetPass(userId, cryPass) ;
    }

    @Override
    public void setPayPass(Long userId, String oldPass, String pass) {
        //给新密码加密
        String cryPass = EndecryptUtil.encrytMd5(pass, CommonConstant.DEFAULT_SALT, CommonConstant.DEFAULT_HASH_COUNT) ;
        LambdaUpdateChainWrapper<User> up = new LambdaUpdateChainWrapper<>(this.baseMapper) ;
        up.set(User::getPayPass, cryPass) ;
        up.eq(User::getId,userId) ;

        up.update() ;
    }

    @Override
    public void resetEmail(Long userId, String email) {
        LambdaUpdateChainWrapper<User> up = new LambdaUpdateChainWrapper<>(this.baseMapper) ;
        up.set(User::getEmail, email) ;
        up.eq(User::getId,userId) ;
    }

    @Override
    public void activeUser(Long userId) {
        LambdaUpdateChainWrapper<User> up = new LambdaUpdateChainWrapper<>(this.baseMapper) ;
        up.eq(User::getId,userId) ;
        up.set(User::getUserStatus, 1) ;

        up.update() ;
    }

    private void resetPass(Long userId, String pass) {
        LambdaUpdateChainWrapper<User> up = new LambdaUpdateChainWrapper<>(this.baseMapper) ;
        up.set(User::getPassword, pass) ;
        up.eq(User::getId, userId) ;

        up.update() ;
    }

    @Override
    public boolean checkPayPass(Long userId, String payPass) {
        User user = this.getById(userId) ;
        if ( StringUtils.isBlank(user.getPayPass()) ) {
            throw new BusinessException("未设置支付密码，请先设置支付密码再进行操作") ;
        }
        String cryPass = EndecryptUtil.encrytMd5(payPass, CommonConstant.DEFAULT_SALT, CommonConstant.DEFAULT_HASH_COUNT) ;

        return StringUtils.equals(user.getPayPass(), cryPass) ;
    }

    @Override
    public Page<User> inviteUser(String code, Page<User> query) {
        QueryWrapper<User> q = new QueryWrapper<>() ;
        q.lambda().select(User::getEmail,User::getUserStatus,User::getCreateTime,User::getPhone)
                .eq(User::getInviteCode,code)
                .orderByDesc(User::getCreateTime) ;

        return this.baseMapper.selectPage(query,q) ;
    }
}
