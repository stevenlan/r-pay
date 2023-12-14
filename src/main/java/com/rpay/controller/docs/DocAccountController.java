package com.rpay.controller.docs;

import com.rpay.common.utils.R;
import com.rpay.controller.BaseController;
import com.rpay.controller.vo.PerVO;
import com.rpay.model.BankDetail;
import com.rpay.model.Countries;
import com.rpay.model.KycCertification;
import com.rpay.model.bill.BalanceDetail;
import com.rpay.service.AccountService;
import com.rpay.service.BalanceService;
import com.rpay.service.UserService;
import com.rpay.service.model.AccountDetail;
import com.rpay.service.query.BankQuery;
import com.rpay.service.query.KycQuery;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @author steven
 */
@Controller
@RequiredArgsConstructor
public class DocAccountController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(this.getClass()) ;

    private final AccountService accountService ;
    private final UserService userService;
    private final BalanceService balService ;

    //获取当前用户状态(账号认证资料审核状态，银行账户申请状态)
    @ApiOperation(value = "获取当前账号的认证信息")
    @GetMapping("doc/api/accountKyc")
    @ResponseBody
    public R<AccountDetail> accountDetail() {
        return R.failed("参数文档，无法调用") ;
    }

    @ApiOperation(value = "获取审核KYC列表")
    @PostMapping("doc/api/kycList")
    @ResponseBody
    public R<List<KycCertification>> kycList(@Valid KycQuery query) {
        return R.failed("参数文档，无法调用") ;
    }

    @ApiOperation(value = "提交KYC信息")
    @PostMapping("doc/api/subKyc")
    @ResponseBody
    public R subKyc(@Valid KycCertification kyc) {
        return R.failed("参数文档，无法调用") ;
    }

    @ApiOperation(value = "获取KYC审核信息")
    @GetMapping("doc/api/getKyc")
    @ResponseBody
    public R<KycCertification> getKyc(Long kycId) {
        return R.failed("参数文档，无法调用") ;
    }

    @ApiOperation(value = "审核KYC认证信息")
    @RequiresPermissions("dir:add")
    @PostMapping("doc/api/perKyc")
    @ResponseBody
    public R perKyc(@Valid PerVO per) {
        return R.failed("参数文档，无法调用") ;
    }

    @ApiOperation(value = "获取银行账户列表")
    @PostMapping("doc/api/bankList")
    @ResponseBody
    public R<List<BankDetail>> bankList(@Valid BankQuery query) {
        return R.failed("参数文档，无法调用") ;
    }

    @ApiOperation(value = "提交银行信息")
    @PostMapping("doc/api/subBank")
    @ResponseBody
    public R subBank(@Valid BankDetail bank) {
        return R.failed("参数文档，无法调用") ;
    }

    @ApiOperation(value = "获取银行账户信息")
    @GetMapping("doc/api/getBank")
    @ResponseBody
    public R<BankDetail> getBank(Long bankId) {
        return R.failed("参数文档，无法调用") ;
    }

    @ApiOperation(value = "删除银行")
    @GetMapping("doc/api/bankDel")
    @ResponseBody
    public R bankDel(Long bankId) {
        return R.failed("参数文档，无法调用") ;
    }

    @ApiOperation(value = "审核银行账户")
    @RequiresPermissions("dir:add")
    @PostMapping("doc/api/perBank")
    @ResponseBody
    public R perBank(@Valid PerVO per) {
        return R.failed("参数文档，无法调用") ;
    }

    @ApiOperation(value = "获取国家列表")
    @GetMapping("doc/api/countries")
    @ResponseBody
    public R<List<Countries>> countries() {
        return R.failed("参数文档，无法调用") ;
    }

    @ApiOperation(value = "获取支持的加密货币列表")
    @GetMapping("doc/api/cryptocurrencies")
    @ResponseBody
    public R<List<Countries>> cryptocurrencies() {
        return R.failed("参数文档，无法调用") ;
    }

    @ApiOperation(value = "获取账户余额列表")
    @GetMapping("doc/api/accountBalance")
    @ResponseBody
    public R<List<BalanceDetail>> accountBalance() {
        return R.failed("参数文档，无法调用") ;
    }
}
