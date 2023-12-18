package com.rpay.controller;

import com.rpay.common.utils.R;
import com.rpay.common.utils.StringUtil;
import com.rpay.controller.vo.CheckVO;
import com.rpay.controller.vo.LoginVO;
import com.rpay.controller.vo.Password;
import com.rpay.mapper.UserRoleMapper;
import com.rpay.model.User;
import com.rpay.model.validate.UserValidated;
import com.rpay.service.UserService;
import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

/**
 * LoginController
 * @author steven
 */
@Api("讲师管理")
@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController extends BaseController {

    private final UserService userService;

    /**
     * 生成验证码 算术类型
     */
    @ApiOperation(value = "获取验证码图片，用<img src=/>即刻显示验证码图片")
    @GetMapping("/api/assets/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SpecCaptcha captcha = new SpecCaptcha(130, 48,5);
        //captcha.text();
        request.getSession().setAttribute("captcha", captcha.text());
        captcha.out(response.getOutputStream());
    }

    @ApiOperation(value = "发送验证码接口，短信和邮件验证码暂无对接，验证码输入8888默认通过")
    @PostMapping("/api/sendCheckCode")
    @ResponseBody
    public R sendCheckCode(HttpServletRequest request,@RequestBody CheckVO check) {
        if ( null != check && StringUtils.isNotBlank(check.getEmail())) {
            //TODO 发送邮件确认码
            request.getSession().removeAttribute("PhoneCode");
            SpecCaptcha captcha = new SpecCaptcha(130, 48,6);
            String code = captcha.text() ;
            request.getSession().setAttribute("MailCode", code) ;

            userService.mailCheckCode(check.getEmail(),code) ;
            return R.succeed("发送成功") ;
        } else if ( null != check && StringUtils.isNotBlank(check.getAreaCode())
                && StringUtils.isNotBlank(check.getPhone()) ) {
            //发送短信确认码
            //request.getSession().removeAttribute("MailCode");
            return R.failed("暂未开通短信通道") ;
        } else {
            return R.failed("未提供邮箱或者完整手机号码") ;
        }
    }

    /**
     * 登录
     */
    @ApiOperation(value = "登录接口，需要接入图片验证码，登录成功后返回对象包含登录用户信息")
    @PostMapping("/api/login")
    @ResponseBody
    public R login(HttpServletRequest request, @Valid @RequestBody LoginVO login) {
        String sessionCode = (String) request.getSession().getAttribute("captcha");
        if ( null == login.getCode() || null == sessionCode || !StringUtils.equalsIgnoreCase(sessionCode,login.getCode().trim()) ) {
            request.getSession().removeAttribute("captcha");
            return R.failed("图形验证码不正确");
        }
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(login.getUsername(), login.getPassword(), null == login.getRememberMe()?Boolean.FALSE : login.getRememberMe());
            SecurityUtils.getSubject().login(token);
            User user = getLoginUser() ;
            user.setAdmin(userService.isAdmin(user)) ;
            return R.succeed(user);
        } catch (UnknownAccountException e) {
            return R.failed("用户不存在");
        } catch (IncorrectCredentialsException e) {
            return R.failed("密码错误");
        } catch (ExcessiveAttemptsException eae) {
            return R.failed("操作频繁，请稍后再试");
        }
    }

    @ApiOperation(value = "登出接口")
    @GetMapping("/api/logout")
    @ResponseBody
    public R logout() {
        out() ;
        return R.succeed("登出成功") ;
    }

    /**
     * 注册
     */
    @ApiOperation(value = "注册接口，需要接入图片验证码，注册成功后返回对象包含登录用户信息，前端注册完成成功后直接进入系统首页")
    @PostMapping("/api/reg")
    @ResponseBody
    public R register(HttpServletRequest request, @Valid @RequestBody User user) {
        String sessionCode = (String) request.getSession().getAttribute("captcha");
        if (user.getCode() == null || null == sessionCode || !StringUtils.equalsIgnoreCase(sessionCode,user.getCode().trim())) {
            request.getSession().removeAttribute("captcha");
            return R.failed("图形验证码不正确");
        }

        String mailCode = (String)request.getSession().getAttribute("MailCode") ;
        String phoneCode = (String)request.getSession().getAttribute("PhoneCode") ;
        if ( !StringUtils.equalsIgnoreCase(mailCode, user.getCheckCode())
                && !StringUtils.equalsIgnoreCase(phoneCode, user.getCheckCode()) ) {
            return R.failed("邮箱或短信确认码不正确或已过期，请重新发送验证码");
        }
        request.getSession().removeAttribute("MailCode");
        request.getSession().removeAttribute("PhoneCode");

        R r = UserValidated.regKeyEmpty(user) ;
        if ( null != r ) {
            return r ;
        }
        String pwd = user.getPassword() ;
        if (userService.addUser(user)) {
            try {
                UsernamePasswordToken token = new UsernamePasswordToken(user.getEmail(), pwd, false);
                SecurityUtils.getSubject().login(token);

                user = getLoginUser() ;
                user.setAdmin(userService.isAdmin(user)) ;
                return R.succeed(user);
            } catch (UnknownAccountException e) {
                return R.failed("用户不存在");
            } catch (IncorrectCredentialsException e) {
                return R.failed("密码错误");
            } catch (ExcessiveAttemptsException eae) {
                return R.failed("操作频繁，请稍后再试");
            }
        }
        return R.failed("注册失败") ;
    }

    /**
     * 忘记密码
     */
    @ApiOperation(value = "忘记密码接口")
    @PostMapping("/api/forgotPwd")
    @ResponseBody
    public R forgotPwd(HttpServletRequest request, @Valid @RequestBody Password password) {
        String mailCode = (String)request.getSession().getAttribute("MailCode") ;
        String phoneCode = (String)request.getSession().getAttribute("PhoneCode") ;
        if ( StringUtils.equalsIgnoreCase(mailCode, password.getCheckCode()) ) {
            userService.resetPass(password.getEmail(),password.getNewPass()) ;
        } else if (StringUtils.equalsIgnoreCase(phoneCode, password.getCheckCode()) ) {
            userService.resetPass(password.getPhone(),password.getNewPass()) ;
        } else {
            return R.failed("邮箱或短信确认码不正确或已过期，请重新发送验证码");
        }
        request.getSession().removeAttribute("MailCode");
        request.getSession().removeAttribute("PhoneCode");

        return R.succeed("修改成功") ;
    }

    /**
     * 重设密码
     */
    @ApiOperation(value = "重设密码接口")
    @PostMapping("/api/resetPwd")
    @ResponseBody
    public R resetPwd(@Valid @RequestBody Password password) {
        userService.resetPass(getLoginUserId(),password.getOldPass(),password.getNewPass()) ;
        return R.succeed("修改成功") ;
    }

    @ApiOperation(value = "设置支付密码")
    @PostMapping("/api/setPayPwd")
    @ResponseBody
    public R setPayPwd(HttpServletRequest request, @Valid @RequestBody Password password) {
        String mailCode = (String)request.getSession().getAttribute("MailCode") ;
        String phoneCode = (String)request.getSession().getAttribute("PhoneCode") ;
        if ( !StringUtils.equalsIgnoreCase(mailCode, password.getCheckCode())
                && !StringUtils.equalsIgnoreCase(phoneCode, password.getCheckCode()) ) {
            return R.failed("邮箱或短信确认码不正确或已过期，请重新发送验证码");
        }
        request.getSession().removeAttribute("MailCode");
        request.getSession().removeAttribute("PhoneCode");

        userService.setPayPass(getLoginUserId(),password.getOldPass(),password.getNewPass());
        return R.succeed("操作成功") ;
    }

    @ApiOperation(value = "设置支付密码")
    @PostMapping("/api/resetEmail")
    @ResponseBody
    public R resetEmail(HttpServletRequest request, @Valid @RequestBody Password password) {
        String mailCode = (String)request.getSession().getAttribute("MailCode") ;
        String phoneCode = (String)request.getSession().getAttribute("PhoneCode") ;
        if ( !StringUtils.equalsIgnoreCase(mailCode, password.getCheckCode())
                && !StringUtils.equalsIgnoreCase(phoneCode, password.getCheckCode()) ) {
            return R.failed("邮箱或短信确认码不正确或已过期，请重新发送验证码");
        }
        request.getSession().removeAttribute("MailCode");
        request.getSession().removeAttribute("PhoneCode");

        userService.resetEmail(getLoginUserId(), password.getEmail());
        return R.succeed("操作成功") ;
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/api/getCur")
    @ResponseBody
    public R<User> getCur() {
        return R.succeed(getLoginUser()) ;
    }

    /*
     * 邮箱中收到了激活邮件后点击激活即可
     * @param request
     * @param code 传入的确认码
     * @return

    @ApiOperation(value = "校验邮箱、短信验证码接口跟sendCheckCode配对使用，目前暂时没用")
    @PostMapping("/active")
    @ResponseBody
    public R active(HttpServletRequest request, String code) {
        if ( userService.checkUserActive(code) ) {
            return R.succeed("激活成功");
        } else {
            return R.failed("激活失败");
        }
    }*/

    /*
     * @deprecated 认证资料补充，需要重新写方法
     * @param ins
     * @return
    @PostMapping("/saveIns")
    @ResponseBody
    public R saveIns(InsVO ins) {
        User user = getLoginUser() ;
        user = userService.getById(user.getId()) ;
        //拼接用户昵称
        Map<String,String> base = ins.baseInfo() ;
        user.setNickName(base.get("companyName"));
        userService.updateById(user) ;

        KycDetail kyc = kycService.renderKyc(base,ins.bankInfo(),ins.attInfo()) ;
        kyc.setProviderId(user.getProviderId()) ;

        KycDetail oldKyc = kycService.lambdaQuery().eq(KycDetail::getProviderId,user.getProviderId()).one() ;
        if ( null == oldKyc) {
            kycService.save(kyc) ;
        } else {
            kyc.setId(oldKyc.getId());
            kycService.updateById(kyc) ;
            userService.lambdaUpdate().set(User::getUserStatus,0).eq(User::getId,user.getId()).update() ;
        }
        return R.succeed("提交kyc资料成功") ;
    }
     */

    /*
     * @deprecated 认证资料补充，需要重新写方法
     * @param ind
     * @return
    @PostMapping("/saveInd")
    @ResponseBody
    public R saveInd(IndVO ind) {
        User user = getLoginUser() ;
        user = userService.getById(user.getId()) ;
        //拼接用户昵称
        Map<String,String> base = ind.baseInfo() ;
        String username = base.get("lastName") + " " + base.get("firstName") ;
        user.setNickName(username);
        userService.updateById(user) ;

        KycDetail kyc = kycService.renderKyc(base,ind.bankInfo(),ind.attInfo()) ;
        kyc.setProviderId(user.getProviderId()) ;

        KycDetail oldKyc = kycService.lambdaQuery().eq(KycDetail::getProviderId,user.getProviderId()).one() ;
        if ( null == oldKyc) {
            kycService.save(kyc) ;
        } else {
            kyc.setId(oldKyc.getId());
            kycService.updateById(kyc) ;
            userService.lambdaUpdate().set(User::getUserStatus,0).eq(User::getId,user.getId()).update() ;
        }

        return R.succeed("提交kyc资料成功") ;
    }
     */
}
