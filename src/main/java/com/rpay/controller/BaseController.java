package com.rpay.controller;

import com.rpay.model.User;
import com.rpay.service.i18n.LocaleMessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author steven
 */
public abstract class BaseController {
    @Autowired
    private LocaleMessageUtil messageSource;

    /**
     * 获取当前登录的user
     */
    public User getLoginUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Object object = subject.getPrincipal();
            if (object != null) {
                User user = (User) object;
                user.setHasPayPass( StringUtils.isNotBlank(user.getPayPass()) );

                //注销掉所有的泄密信息
                user.setPassword(null);
                user.setCheckCode(null);
                user.setRoleList(null);
                user.setAuthList(null);
                user.setParentId(null);
                user.setPayPass(null);
                return user ;
            }
        }
        return null;
    }

    /**
     * 获取当前登录的userId
     */
    public Long getLoginUserId() {
        User loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getId();
    }

    public void out() {
        Subject subject = SecurityUtils.getSubject();
        if ( null != subject ) {
            subject.logout() ;
        }
    }

    public String coverString(String el) {
        if (StringUtils.contains(el,"{")
                && StringUtils.contains(el,"}")) {
            el = StringUtils.substringBetween(el,"{","}") ;
            return messageSource.getMessage(el) ;
        }
        return el ;
    }
}
