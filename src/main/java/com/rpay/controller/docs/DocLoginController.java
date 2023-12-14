package com.rpay.controller.docs;

import com.rpay.common.utils.R;
import com.rpay.controller.BaseController;
import com.rpay.controller.vo.CheckVO;
import com.rpay.controller.vo.LoginVO;
import com.rpay.model.User;
import com.rpay.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * LoginController
 * @author steven
 */
@Api("讲师管理")
@Slf4j
@Controller
@RequiredArgsConstructor
public class DocLoginController extends BaseController {

    private final UserService userService;

    /**
     * 生成验证码 算术类型
     */
    @ApiOperation(value = "获取验证码图片，用<img src=/>即刻显示验证码图片")
    @GetMapping("doc/api/assets/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
    }

    @ApiOperation(value = "发送验证码接口，短信和邮件验证码暂无对接，验证码输入8888默认通过")
    @PostMapping("doc/api/sendCheckCode")
    @ResponseBody
    public R sendCheckCode(HttpServletRequest request, CheckVO check) {
        return R.failed("注册失败") ;
    }

    /**
     * 登录
     */
    @ApiOperation(value = "登录接口，需要接入图片验证码，登录成功后返回对象包含登录用户信息")
    @PostMapping("doc/api/login")
    @ResponseBody
    public R login(HttpServletRequest request, @Valid LoginVO login) {
        return R.failed("注册失败") ;
    }

    @ApiOperation(value = "登出接口")
    @GetMapping("doc/api/logout")
    @ResponseBody
    public R logout() {
        return R.failed("注册失败") ;
    }

    /**
     * 注册
     */
    @ApiOperation(value = "注册接口，需要接入图片验证码，注册成功后返回对象包含登录用户信息，前端注册完成成功后直接进入系统首页")
    @PostMapping("doc/api/reg")
    @ResponseBody
    public R register(HttpServletRequest request, @Valid User user) {
        return R.failed("注册失败") ;
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
