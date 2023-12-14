package com.rpay.model.validate;

import com.rpay.common.utils.R;
import com.rpay.common.utils.StringUtil;
import com.rpay.model.User;

/**
 * 模型验证代码，将对应的模型验证放在这
 * @author steven
 */
public interface UserValidated {

    /**
     * 判断用户数据是否符合业务约束
     * @param user
     * @return
     */
    static R regKeyEmpty(User user) {
        if ( StringUtil.isBlank(user.getInviteCode()) ) {
            return R.failed("没有推荐人，无法注册");
        }

        //手机号和邮箱必填一个
        if ( !isKeyProEmpty(user) ) {
            return R.failed("账号或者手机号码不能为空");
        }
        if (StringUtil.isBlank(user.getPassword())) {
            return R.failed("密码不能为空");
        }
        return null ;
    }

    /**
     * 手机号和邮箱必填其中一个
     * @param user
     * @return
     */
    static boolean isKeyProEmpty(User user) {
        return StringUtil.isNotBlank(user.getEmail())
                || (StringUtil.isNotBlank(user.getPhone()) && StringUtil.isNotBlank(user.getAreaCode())) ;
    }
}
