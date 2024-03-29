package com.rpay.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.common.utils.R;
import com.rpay.controller.vo.PerVO;
import com.rpay.model.*;
import com.rpay.model.bill.BalanceDetail;
import com.rpay.model.bill.BillDetail;
import com.rpay.model.validate.group.BankNor;
import com.rpay.model.validate.group.BankSimple;
import com.rpay.service.AccountService;
import com.rpay.service.BalanceService;
import com.rpay.service.BillService;
import com.rpay.service.UserService;
import com.rpay.service.model.AccountDetail;
import com.rpay.service.query.BillDetailQuery;
import com.rpay.service.query.BankQuery;
import com.rpay.service.query.KycQuery;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.ListUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author steven
 */
@Controller
@RequiredArgsConstructor
public class AccountController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(this.getClass()) ;

    private final AccountService accountService ;
    private final UserService userService;
    private final BalanceService balService ;
    private final BillService billService ;
    private final SmartValidator validator ;

    //获取当前用户状态(账号认证资料审核状态，银行账户申请状态)
    @ApiOperation(value = "获取当前账号的认证信息")
    @GetMapping("/api/accountKyc")
    @ResponseBody
    public R<AccountDetail> accountDetail() {
        Long userId = getLoginUserId() ;
        KycCertification kyc = accountService.findKyc(userId) ;
        AccountDetail detail = new AccountDetail() ;
        detail.setKyc(kyc) ;

        detail.setBanks(accountService.bankCount(userId, false));
        detail.setAuthBanks(accountService.bankCount(userId, true));

        return R.succeed(detail) ;
    }

    @ApiOperation(value = "获取审核KYC列表")
    @PostMapping("/api/kycList")
    @ResponseBody
    public R<List<KycCertification>> kycList(@Valid @RequestBody KycQuery query) {
        if ( null == query ) {
            query = new KycQuery() ;
        }
        if ( !userService.isAdmin(getLoginUser()) ) {
            query.setUserId(getLoginUserId()) ;
        }
        List<KycCertification> kycs = accountService.findKycList(query) ;
        return R.succeed(kycs) ;
    }

    @ApiOperation(value = "获取审核KYC列表")
    @PostMapping("/api/kycPage")
    @ResponseBody
    public R<Page<KycCertification>> kycPage(@Valid @RequestBody KycQuery query) {
        if ( null == query ) {
            query = new KycQuery() ;
        }
        if ( !userService.isAdmin(getLoginUser()) ) {
            query.setUserId(getLoginUserId()) ;
        }
        //List<KycCertification> kycs = accountService.findKycList(query) ;
        Page<KycCertification> kycPage = accountService.pageKycList(query) ;
        return R.succeed(kycPage) ;
    }

    @ApiOperation(value = "提交KYC信息")
    @PostMapping("/api/subKyc")
    @ResponseBody
    public R subKyc(@Valid @RequestBody KycCertification kyc) {
        kyc.setUserId(getLoginUserId()) ;
        if (accountService.updateKyc(kyc)) {
            return R.succeed(coverString("{sys.save.success}")) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "获取KYC审核信息")
    @GetMapping("/api/getKyc")
    @ResponseBody
    public R<KycCertification> getKyc(Long kycId) {
        KycCertification kyc = null ;
        if ( null == kycId ) {
            kyc = accountService.findKyc(getLoginUserId());
        } else {
            kyc = accountService.findKycForId(kycId) ;
        }
        return R.succeed(kyc) ;
    }

    @ApiOperation(value = "审核KYC认证信息")
    @RequiresPermissions("dir:add")
    @PostMapping("/api/perKyc")
    @ResponseBody
    public R perKyc(@Valid @RequestBody PerVO per) {
        if ( accountService.passKyc(per.getId(),per.getPass(),per.getReason()) ) {
            return R.succeed(coverString("{sys.op.success}")) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "获取银行账户列表")
    @PostMapping("/api/bankList")
    @ResponseBody
    public R<List<BankDetail>> bankList(@Valid @RequestBody BankQuery query) {
        if ( null == query ) {
            query = new BankQuery() ;
        }
        if ( !userService.isAdmin(getLoginUser()) ) {
            query.setUserId(getLoginUserId()) ;
        }
        List<BankDetail> banks = accountService.findBanks(query) ;
        return R.succeed(banks) ;
    }

    @ApiOperation(value = "获取用户指定货币绑定的银行账户列表")
    @GetMapping("/api/coinBanks")
    @ResponseBody
    public R<List<BankDetail>> coinBanks(String coin) {
        return R.succeed(accountService.coinBanks(coin)) ;
    }

    @ApiOperation(value = "获取银行账户列表")
    @PostMapping("/api/bankPage")
    @ResponseBody
    public R<Page<BankDetail>> bankPage(@Valid @RequestBody BankQuery query) {
        if ( null == query ) {
            query = new BankQuery() ;
        }
        if ( !userService.isAdmin(getLoginUser()) ) {
            query.setUserId(getLoginUserId()) ;
        }
        Page<BankDetail> bankPage = accountService.pageBanksByUser(query) ;
        return R.succeed(bankPage) ;
    }

    @ApiOperation(value = "提交银行信息")
    @PostMapping("/api/subBank")
    @ResponseBody
    public R subBank(HttpServletRequest request, @RequestBody BankDetail bank) throws BindException {
        BindException bindException = new BindException(bank, BankDetail.class.getName());
        if ( StringUtils.isNotBlank(bank.getBankCountry())
                && (StringUtils.equalsIgnoreCase("ph", bank.getBankCountry())
                || StringUtils.equalsIgnoreCase("ng", bank.getBankCountry()) ) ) {
            validator.validate(bank, bindException, BankSimple.class) ;
        } else {
            validator.validate(bank, bindException, BankNor.class) ;
        }
        if ( bindException.hasErrors() ) {
            throw bindException ;
        }

        if ( StringUtils.isBlank(bank.getPayPass()) && StringUtils.isBlank(bank.getVerCode()) ) {
            return R.failed(coverString("{sys.pay.empty}"));
        }
        if ( ListUtils.isEmpty(bank.getBindCoins()) ) {
            return R.failed(coverString("{bank.coin.empty}"));
        }
        if ( StringUtils.isBlank(bank.getPayPass()) ) {
            //验证码校验，或者支付密码校验
            String mailCode = (String)request.getSession().getAttribute("MailCode") ;
            String phoneCode = (String)request.getSession().getAttribute("PhoneCode") ;
            if ( !StringUtils.equalsIgnoreCase(mailCode, bank.getVerCode())
                    && !StringUtils.equalsIgnoreCase(phoneCode, bank.getVerCode()) ) {
                return R.failed(coverString("{sys.sms.valid}"));
            }
            request.getSession().removeAttribute("MailCode");
            request.getSession().removeAttribute("PhoneCode");
        }
        bank.setUserId(getLoginUserId()) ;
        if ( accountService.updateAccount(bank) ) {
            return R.succeed(coverString("{sys.op.success}")) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "获取银行账户信息")
    @GetMapping("/api/getBank")
    @ResponseBody
    public R<BankDetail> getBank(Long bankId) {
        BankDetail bank = accountService.findBank(bankId) ;
        return R.succeed(bank) ;
    }

    @ApiOperation(value = "删除银行")
    @PostMapping("/api/bankDel")
    @ResponseBody
    public R bankDel(HttpServletRequest request, @RequestBody BankDetail bank) {
        if ( userService.isAdmin(getLoginUser()) ) {
            if (accountService.bankDel(bank, null)) {
                return R.succeed(coverString("{sys.op.success}")) ;
            }
        } else {
            if ( StringUtils.isBlank(bank.getPayPass()) && StringUtils.isBlank(bank.getVerCode()) ) {
                return R.failed(coverString("{sys.pay.empty}"));
            }
            if ( StringUtils.isBlank(bank.getPayPass()) ) {
                //验证码校验，或者支付密码校验
                String mailCode = (String)request.getSession().getAttribute("MailCode") ;
                String phoneCode = (String)request.getSession().getAttribute("PhoneCode") ;
                if ( !StringUtils.equalsIgnoreCase(mailCode, bank.getVerCode())
                        && !StringUtils.equalsIgnoreCase(phoneCode, bank.getVerCode()) ) {
                    return R.failed(coverString("{sys.sms.valid}"));
                }
                request.getSession().removeAttribute("MailCode");
                request.getSession().removeAttribute("PhoneCode");
            }
            if (accountService.bankDel(bank, getLoginUserId()) ) {
                return R.succeed(coverString("{sys.op.success}")) ;
            }
        }
        return R.failed(coverString("{sys.op.unAuth}")) ;
    }

    @ApiOperation(value = "审核银行账户")
    @RequiresPermissions("dir:add")
    @PostMapping("/api/perBank")
    @ResponseBody
    public R perBank(@Valid @RequestBody PerVO per) {
        if (accountService.passBank(per.getId(),per.getPass(),per.getReason())) {
            return R.succeed(coverString("{sys.op.success}")) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "获取国家列表")
    @GetMapping("/api/countries")
    @ResponseBody
    public R<List<Countries>> countries() {
        return R.succeed(accountService.getCountries()) ;
    }

    @ApiOperation(value = "获取国家列表")
    @GetMapping("/api/allCoin")
    @ResponseBody
    public R<List<Countries>> allCoin() {
        return R.succeed(accountService.getCoins()) ;
    }

    @ApiOperation(value = "获取支持的加密货币列表")
    @GetMapping("/api/cryptocurrencies")
    @ResponseBody
    public R<List<Countries>> cryptocurrencies() {
        return R.succeed(accountService.getCrypts()) ;
    }

    @ApiOperation(value = "获取账户余额列表")
    @GetMapping("/api/accountBalance")
    @ResponseBody
    public R<List<BalanceDetail>> accountBalance() {
        List<BalanceDetail> balList = balService.queryBalances(getLoginUserId()) ;
        return R.succeed(balList) ;
    }

    //status:1
    @ApiOperation(value = "余额变更详情列表查询")
    @PostMapping("/api/billDetails")
    @ResponseBody
    public R<Page<BillDetail>> billDetails(@Valid @RequestBody BillDetailQuery query) {
        return R.succeed(billService.balDetails(query)) ;
    }

    //查看推荐用户
    @ApiOperation(value = "获取当前登录用户的推荐用户列表")
    @PostMapping("/api/inviteUser")
    @ResponseBody
    public R<Page<User>> inviteUser(@Valid @RequestBody Page<User> query) {
        User u = userService.getById(getLoginUserId()) ;
        Page<User> result = userService.inviteUser(u.getProviderId(),query) ;

        //脱敏
        if ( result.getTotal() > 0 ) {
            result.getRecords().forEach(user -> {
                if ( StringUtils.isNotBlank(user.getEmail()) ) {
                    String com = StringUtils.substringAfter(user.getEmail(),"@") ;
                    String name = StringUtils.substringBefore(user.getEmail(),"@") ;
                    if ( name.length() > 3 ) {
                        String m = StringUtils.substring(name,0,3) ;
                        user.setEmail(m+"****@"+com) ;
                    } else {
                        user.setEmail(name+"****@"+com) ;
                    }
                }
                if ( StringUtils.isNotBlank(user.getPhone()) ) {
                    if ( user.getPhone().length() > 4 ) {
                        String p = StringUtils.substring(user.getPhone(),0,4) ;
                        user.setPhone(p+"******");
                    }
                }
            });
        }
        return R.succeed(result) ;
    }
}
