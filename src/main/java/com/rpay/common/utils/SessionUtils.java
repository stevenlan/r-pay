package com.rpay.common.utils;

import com.rpay.model.Exchange;
import com.rpay.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author steven
 */
public interface SessionUtils {
    /**
     * 获取登录用户
     * @return
     */
    default User getLoginUser() {
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
                user.setProviderId(null);
                user.setParentId(null);
                user.setPayPass(null);
                return user ;
            }
        }
        return null;
    }

    /**
     * 获取当前登录的userId
     * @return
     */
    default Long getLoginUserId() {
        User loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getId();
    }

    /**
     * 计算转汇
     * @param change
     * @param value
     * @return
     */
    default Double comChange(Exchange change, Double value) {
        String b1 = value.toString() ;
        String b2 = change.getExRate().toString() ;
        BigDecimal src = new BigDecimal(b1) ;
        BigDecimal rate = new BigDecimal(b2) ;

        DecimalFormat ret = new DecimalFormat("#0.000") ;
        ret.setRoundingMode(RoundingMode.FLOOR);
        String retValue = ret.format(src.multiply(rate).doubleValue()) ;
        return new BigDecimal(retValue).doubleValue() ;
    }

    /**
     * 计算差值
     * @param reqValue
     * @param commission
     * @return
     */
    default Double commissionReq(Double reqValue, Float commission) {
        String total = reqValue.toString() ;
        String com = commission.toString() ;
        BigDecimal t = new BigDecimal(total) ;
        BigDecimal c = new BigDecimal(com) ;

        return t.subtract(c).doubleValue() ;
    }
}
