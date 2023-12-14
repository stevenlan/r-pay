package com.rpay.common.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rpay.common.constant.CommonConstant;
import com.rpay.mapper.RoleAuthMapper;
import com.rpay.mapper.UserMapper;
import com.rpay.mapper.UserRoleMapper;
import com.rpay.model.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义认证授权处理逻辑
 *
 * @author dinghao
 * @date 2021/3/12
 */
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleAuthMapper roleAuthMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<String> roleList = userRoleMapper.selectRoleCodesById(user.getId());
        List<String> authList = roleAuthMapper.selectAuthCodesByRoleCodes(roleList);
        Set<String> roleSet = new HashSet<>(roleList);
        Set<String> authSet = new HashSet<>(authList);
        authSet.add("status:"+user.getUserStatus()) ;
        authorizationInfo.setRoles(roleSet);
        authorizationInfo.setStringPermissions(authSet);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        // 往数据库中查询用户
        User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getPhone, username));
        if (user == null) {
            user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getEmail, username));
        }
        if (user == null) {
            // 账号不存在
            throw new UnknownAccountException();
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(CommonConstant.DEFAULT_SALT), getName());

    }

}
