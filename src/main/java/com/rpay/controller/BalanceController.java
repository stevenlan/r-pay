package com.rpay.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.common.exception.BusinessException;
import com.rpay.common.utils.R;
import com.rpay.common.utils.SessionUtils;
import com.rpay.controller.vo.PerReqVO;
import com.rpay.model.BankDetail;
import com.rpay.model.CryptRequest;
import com.rpay.model.Deposit;
import com.rpay.model.Exchange;
import com.rpay.model.bill.BalanceDetail;
import com.rpay.model.deposit.DepRequest;
import com.rpay.model.withdraw.WithdrawRequest;
import com.rpay.service.AccountService;
import com.rpay.service.BalanceService;
import com.rpay.service.ExchangeService;
import com.rpay.service.UserService;
import com.rpay.service.query.*;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author steven
 */
@Controller
@RequiredArgsConstructor
public class BalanceController extends BaseController implements SessionUtils {
    private final BalanceService balService ;
    private final ExchangeService exService ;
    private final AccountService accService ;
    private final UserService userService;

    @ApiOperation(value = "入金申请deposit提交接口，用户提交分成两步，第一步提交申请表单数据，第二部提交转账凭证")
    @PostMapping("/api/putDeposit")
    @ResponseBody
    public R putDeposit(@Valid @RequestBody DepRequest dep) {
        DepRequest ret = balService.putDepReq(dep) ;
        if ( null != ret ) {
            return R.succeed(ret) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }
    @ApiOperation(value = "入金申请列表查询，带分页数据")
    @PostMapping("/api/depositList")
    @ResponseBody
    public R<Page<DepRequest>> depositList(@Valid @RequestBody DepositQuery<DepRequest> query) {
        if (!userService.isAdmin(getLoginUser())) {
            query.setEmail(getLoginUser().getEmail()) ;
        }
        Page<DepRequest> ret = balService.queryDepList(query) ;
        return R.succeed(ret) ;
    }

    @ApiOperation(value = "入金申请详情")
    @GetMapping("/api/depositLGet")
    @ResponseBody
    public R<DepRequest> depositLGet(Long id) {
        DepRequest req = balService.findDepRequest(id) ;
        return R.succeed(req) ;
    }

    @ApiOperation(value = "入金审核（接口会判断admin权限）审核由财务审核，财务审核完成后就确认入账")
    @PostMapping("/api/perDeposit")
    @RequiresRoles("admin")
    @ResponseBody
    public R perDeposit(@Valid @RequestBody PerReqVO per) {
        if (balService.perDeposit(per) ) {
            return R.succeed(coverString("{sys.op.success}")) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "取消法币入账申请，状态为1时可以取消")
    @GetMapping("/api/cancelDeposit")
    @ResponseBody
    public R cancelDeposit(Long reqId) {
        if (balService.cancelDeposit(reqId)){
            return R.succeed(coverString("{sys.op.success}")) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "加密货币入金申请提交接口")
    @PostMapping("/api/putCryptDeposit")
    @ResponseBody
    public R putCryptDeposit(@Valid @RequestBody CryptRequest req) {
        req.setReqType(1);
        CryptRequest ret = balService.putCryptRequest(req) ;
        if ( null != ret ) {
            return R.succeed(ret) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "加密货币入金列表查询，带分页")
    @PostMapping("/api/cryptDepositList")
    @ResponseBody
    public R<Page<CryptRequest>> cryptDepositList(@Valid @RequestBody CryQuery<CryptRequest> query) {
        if (!userService.isAdmin(getLoginUser())) {
            query.setEmail(getLoginUser().getEmail()) ;
        }
        query.setType(1);
        Page<CryptRequest> ret = balService.queryCryReq(query) ;
        return R.succeed(ret) ;
    }

    @ApiOperation(value = "加密货币入金详情")
    @GetMapping("/api/cryptDepositGet")
    @ResponseBody
    public R<CryptRequest> cryptDepositGet(Long id) {
        CryptRequest req = balService.findCryReq(id) ;
        return R.succeed(req) ;
    }

    @ApiOperation(value = "入金审核（admin）")
    @PostMapping("/api/perCryptDeposit")
    @RequiresRoles("admin")
    @ResponseBody
    public R perCryptDeposit(@Valid @RequestBody PerReqVO per) {
        if (balService.perCryReq(per)){
            return R.succeed(coverString("{sys.op.success}")) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "出金申请withdraw")
    @PostMapping("/api/putWithdraw")
    @ResponseBody
    public R putWithdraw(HttpServletRequest request, @Valid @RequestBody WithdrawRequest withdraw) {
        if ( StringUtils.isBlank(withdraw.getPayPass()) && StringUtils.isBlank(withdraw.getVerCode()) ) {
            return R.failed(coverString("{sys.pay.empty}"));
        }
        if (StringUtils.isBlank(withdraw.getPayPass()) ) {
            //验证码校验，或者支付密码校验
            String mailCode = (String) request.getSession().getAttribute("MailCode");
            String phoneCode = (String) request.getSession().getAttribute("PhoneCode");
            if (!StringUtils.equalsIgnoreCase(mailCode, withdraw.getVerCode())
                    && !StringUtils.equalsIgnoreCase(phoneCode, withdraw.getVerCode())) {
                return R.failed(coverString("{sys.sms.valid}"));
            }
            request.getSession().removeAttribute("MailCode");
            request.getSession().removeAttribute("PhoneCode");
        }
        WithdrawRequest ret = balService.putWithdrawReq(withdraw) ;
        if ( null != ret ) {
            return R.succeed(ret) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "出金列表查询，带分页")
    @PostMapping("/api/withdrawList")
    @ResponseBody
    public R<Page<WithdrawRequest>> withdrawList(@Valid @RequestBody WithdrawQuery<WithdrawRequest> query) {
        if (!userService.isAdmin(getLoginUser())) {
            query.setEmail(getLoginUser().getEmail()) ;
        }
        Page<WithdrawRequest> ret = balService.queryWitList(query) ;
        return R.succeed(ret) ;
    }

    @ApiOperation(value = "出金审核（admin）--> 审核分为两道，第一次审核，状态是1时，审核通过状态变为2，当状态是2时，审核需要填写实际出款金额和手续费以及上传银行汇款水单")
    @PostMapping("/api/perWithdraw")
    @RequiresRoles("admin")
    @ResponseBody
    public R perWithdraw(@Valid @RequestBody PerReqVO per) {
        if ( balService.perWithdrawRequest(per) ) {
            return R.succeed(coverString("{sys.op.success}")) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "取消法币提现申请，状态为1时可以取消")
    @GetMapping("/api/cancelWithdraw")
    @ResponseBody
    public R cancelWithdraw(Long reqId) {
        if (balService.cancelWithdraw(reqId)){
            return R.succeed(coverString("{sys.op.success}")) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "加密货币出金申请withdraw")
    @PostMapping("/api/putCryptWithdraw")
    @ResponseBody
    public R putCryptWithdraw(HttpServletRequest request, @Valid @RequestBody CryptRequest req) {
        if ( StringUtils.isBlank(req.getPayPass()) && StringUtils.isBlank(req.getVerCode()) ) {
            return R.failed(coverString("{sys.pay.empty}"));
        }
        if (StringUtils.isBlank(req.getPayPass()) ) {
            //验证码校验，或者支付密码校验
            String mailCode = (String) request.getSession().getAttribute("MailCode");
            String phoneCode = (String) request.getSession().getAttribute("PhoneCode");
            if (!StringUtils.equalsIgnoreCase(mailCode, req.getVerCode())
                    && !StringUtils.equalsIgnoreCase(phoneCode, req.getVerCode())) {
                return R.failed(coverString("{sys.sms.valid}"));
            }
            request.getSession().removeAttribute("MailCode");
            request.getSession().removeAttribute("PhoneCode");
        }
        req.setReqType(2);
        CryptRequest ret = balService.putCryptRequest(req) ;
        if ( null != ret ) {
            return R.succeed(ret) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "加密货币出金列表查询，带分页")
    @PostMapping("/api/cryptWithdrawList")
    @ResponseBody
    public R<Page<CryptRequest>> cryptWithdrawList(@Valid @RequestBody CryQuery<CryptRequest> query) {
        if (!userService.isAdmin(getLoginUser())) {
            query.setEmail(getLoginUser().getEmail()) ;
        }
        query.setType(2);
        Page<CryptRequest> ret = balService.queryCryReq(query) ;
        return R.succeed(ret) ;
    }

    @ApiOperation(value = "加密货币出金详情")
    @GetMapping("/api/cryptWithdrawGet")
    @ResponseBody
    public R<CryptRequest> cryptWithdrawGet(Long id) {
        CryptRequest wit = balService.findCryReq(id) ;
        return R.succeed(wit) ;
    }

    @ApiOperation(value = "加密货币出金审核（admin）--> 状态是1时，审核直接就是已转出，需要提交钱包转账截图")
    @PostMapping("/api/perCryptWithdraw")
    @RequiresRoles("admin")
    @ResponseBody
    public R perCryptWithdraw(@Valid @RequestBody PerReqVO per) {
        if (balService.perCryReq(per)){
            return R.succeed(coverString("{sys.op.success}")) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "取消加密提现申请，状态为1时可以取消")
    @GetMapping("/api/cancelCryptWithdraw")
    @ResponseBody
    public R cancelCryptWithdraw(Long cryptId) {
        if (balService.cancelReq(cryptId)){
            return R.succeed(coverString("{sys.op.success}")) ;
        }
        return R.failed(coverString("{sys.op.failed}")) ;
    }

    @ApiOperation(value = "可选择入金货币")
    @GetMapping("/api/depCoins")
    @ResponseBody
    public R<List<Deposit>> depCoins() {
        List<Deposit> depList = exService.deposits() ;
        return R.succeed(depList) ;
    }

    @ApiOperation(value = "可选择出金账本列表")
    @GetMapping("/api/balanceList")
    @ResponseBody
    public R<List<BalanceDetail>> balanceList() {
        List<BalanceDetail> balList = balService.queryBalances(getLoginUserId()) ;
        return R.succeed(balList) ;
    }

    @ApiOperation(value = "管理端查询指定用户全部账户余额信息")
    @GetMapping("/api/userBalanceList")
    @RequiresRoles("admin")
    @ResponseBody
    public R<List<BalanceDetail>> userBalanceList(Long id) {
        List<BalanceDetail> balList = balService.queryBalances(id) ;
        return R.succeed(balList) ;
    }

    @ApiOperation(value = "可选择选择出金币种")
    @GetMapping("/api/withdrawCoins")
    @ResponseBody
    public R<List<String>> withdrawCoins(String code, Integer type) {
        /*兑换不做过滤 if ( null == type || type > 2) {
            type = 1 ;
        }*/
        List<String> coinCodes = balService.withdrawCoins(code, type) ;
        return R.succeed(coinCodes) ;
    }

    @ApiOperation(value = "选择出金账户列表")
    @GetMapping("/api/withdrawAccounts")
    @ResponseBody
    public R<List<BankDetail>> withdrawAccounts() {
        BankQuery query = new BankQuery() ;
        query.setUserId(getLoginUserId()) ;
        query.setBankStatus(1) ;
        List<BankDetail> banks = accService.findBanks(query) ;
        return R.succeed(banks) ;
    }

    @ApiOperation(value = "传入来源货币代码，提现金额，目标货币代码，会计算出预计目标货币金额")
    @PostMapping("/api/calculateRate")
    @ResponseBody
    public R<ExQuery> calculateRate(@Valid @RequestBody ExQuery change) {
        Exchange ex = accService.matchExchange(change) ;
        if ( null == ex ) {
            return R.failed(coverString("{change.coins.support}")) ;
        }
        change.setTargetValue(comChange(ex, change.getExValue())) ;
        return R.succeed(change) ;
    }
}
