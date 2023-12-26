package com.rpay.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.common.exception.BusinessException;
import com.rpay.common.utils.R;
import com.rpay.model.*;
import com.rpay.model.validate.group.AdminGroup;
import com.rpay.model.validate.group.BankNor;
import com.rpay.model.validate.group.UserGroup;
import com.rpay.service.AccountService;
import com.rpay.service.ExchangeService;
import com.rpay.service.UserService;
import com.rpay.service.query.CryAccQuery;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.ListUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 汇率固定设置兑USDT和美金的交易对
 * @author steven
 */
@Controller
@RequiredArgsConstructor
public class ExRateController extends BaseController{
    private final ExchangeService exService ;
    private final AccountService accService ;
    private final UserService userService ;

    @ApiOperation(value = "设置汇率对的汇率价格")
    @PostMapping("/api/setExchange")
    @RequiresRoles("admin")
    @ResponseBody
    public R setExchange(@Valid @RequestBody Exchange ex) {
        if ( accService.updateEx(ex) ) {
            return R.succeed(coverString("{sys.op.success}")) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "获取所支持的汇率对价格列表")
    @GetMapping("/api/getExchanges")
    @ResponseBody
    public R<List<Exchange>> getExchanges() {
        List<Exchange> exs = exService.exchanges() ;
        return R.succeed(exs) ;
    }

    @ApiOperation(value = "获取所支持的汇率对价格分页列表")
    @PostMapping("/api/exchangesPage")
    @ResponseBody
    public R<Page<Exchange>> exchangesPage(@RequestBody Page<Exchange> query) {
        Page<Exchange> exs = exService.exchangesPage(query) ;
        return R.succeed(exs) ;
    }

    @ApiOperation(value = "删除汇率对")
    @GetMapping("/api/delExchange")
    @RequiresRoles("admin")
    @ResponseBody
    public R delExchange(Long id) {
        if ( exService.delExchange(id) ) {
            return R.succeed(coverString("{sys.op.success}")) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "设置目前支持的收款户货币银行账户")
    @PostMapping("/api/setDeposit")
    @RequiresRoles("admin")
    @ResponseBody
    public R setDeposit(@Validated(BankNor.class) @RequestBody BankDetail deposit) {
        if (StringUtils.isBlank(deposit.getCoinCode())){
            R.failed(coverString("{bank.coin.empty}")) ;
        }
        deposit.setUserId(getLoginUserId());
        if ( exService.updateDeposit(deposit) ){
            return R.succeed(coverString("{sys.op.success}")) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "获取平台收款户货币银行账户")
    @GetMapping("/api/getDeposits")
    @ResponseBody
    public R<List<Deposit>> getDeposits() {
        return R.succeed(exService.deposits()) ;
    }

    @ApiOperation(value = "获取平台收款户货币银行账户分页数据")
    @PostMapping("/api/depositsPage")
    @ResponseBody
    public R<Page<Deposit>> depositsPage(@RequestBody Page<Deposit> query) {
        return R.succeed(exService.depositPage(query)) ;
    }

    @ApiOperation(value = "删除收款账户")
    @GetMapping("/api/delDeposit")
    @RequiresRoles("admin")
    @ResponseBody
    public R delDeposit(Long id) {
        if (exService.delDeposit(id)) {
            return R.succeed(coverString("{sys.op.success}")) ;
        } else {
            return R.failed(coverString("{sys.op.failed}")) ;
        }
    }

    @ApiOperation(value = "给认证完成的用户设置加密钱包地址，修改和新增都是用同一个接口")
    @PostMapping("/api/setCryAcc")
    @RequiresRoles("admin")
    @ResponseBody
    public R setCryAcc(@Validated(AdminGroup.class) @RequestBody CryAccount crt) {
        crt.setCryType(0) ;
        crt.setAgreement("TRC20");
        crt.setCryAdd(crt.getTrcAdd());
        exService.updateCry(crt) ;
        crt.setId(null);
        crt.setAgreement("ERC20");
        crt.setCryAdd(crt.getErcAdd());
        exService.updateCry(crt) ;
        return R.succeed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "用户设置提款加密钱包地址，修改和新增都是用同一个接口")
    @PostMapping("/api/setOutCryAcc")
    @ResponseBody
    public R setOutCryAcc(HttpServletRequest request, @Validated(UserGroup.class) @RequestBody CryAccount crt) {
        if ( !StringUtils.equals(crt.getAgreement(),"ERC20") && !StringUtils.equals(crt.getAgreement(),"TRC20") ) {
            throw new BusinessException(coverString("{cryAcc.agreement.support}")) ;
        }
        if ( StringUtils.isBlank(crt.getPayPass()) && StringUtils.isBlank(crt.getVerCode()) ) {
            return R.failed(coverString("{sys.pay.empty}"));
        }
        if ( StringUtils.isBlank(crt.getPayPass()) ) {
            //验证码校验，或者支付密码校验
            String mailCode = (String)request.getSession().getAttribute("MailCode") ;
            String phoneCode = (String)request.getSession().getAttribute("PhoneCode") ;
            if ( !StringUtils.equalsIgnoreCase(mailCode, crt.getVerCode())
                    && !StringUtils.equalsIgnoreCase(phoneCode, crt.getVerCode()) ) {
                return R.failed(coverString("{sys.sms.valid}"));
            }
            request.getSession().removeAttribute("MailCode");
            request.getSession().removeAttribute("PhoneCode");
        }

        crt.setCryType(1) ;
        crt.setUserId(getLoginUserId()) ;
        if (exService.updateOutCry(crt)) {
            return R.succeed(coverString("{sys.op.success}")) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "用户提款地址分页查询")
    @PostMapping("/api/outCryAccPage")
    @ResponseBody
    public R<Page<CryAccount>> outCryAccPage(@RequestBody CryAccQuery query) {
        return R.succeed(exService.pageCryAcc(query)) ;
    }

    @ApiOperation(value = "用户收款地址下拉选项列表")
    @GetMapping("/api/outCryAccList")
    @ResponseBody
    public R<List<CryAccount>> outCryAccList(String agreement, String cryCode) {
        return R.succeed(exService.listCryAcc(agreement, cryCode)) ;
    }

    @ApiOperation(value = "获取钱包地址")
    @GetMapping("/api/getCryAdd")
    @ResponseBody
    public R<List<CryAccount>> getCryAdd(String cryCode, Long userId) {
        if ( StringUtils.isBlank(cryCode) ) {
            return R.failed(coverString("{cryAcc.cryCode.empty}")) ;
        }
        if ( !userService.isAdmin(getLoginUser()) ) {
            userId = getLoginUserId() ;
        }
        List<CryAccount> cry = exService.findCry(cryCode,userId) ;
        if ( !ListUtils.isEmpty(cry) || userService.isAdmin(getLoginUser()) ) {
            return R.succeed(cry) ;
        }
        return R.failed(coverString("{cryAcc.depAdd.empty}")) ;
    }
    //收款管理，汇兑管理，付款管理
}
