package com.rpay.controller.docs;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.common.utils.R;
import com.rpay.common.utils.SessionUtils;
import com.rpay.controller.BaseController;
import com.rpay.controller.vo.PerReqVO;
import com.rpay.model.BankDetail;
import com.rpay.model.CryptRequest;
import com.rpay.model.Deposit;
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
import org.apache.shiro.authz.annotation.RequiresRoles;
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
public class DocBalanceController extends BaseController implements SessionUtils {
    private final BalanceService balService ;
    private final ExchangeService exService ;
    private final AccountService accService ;
    private final UserService userService;

    @ApiOperation(value = "入金申请deposit提交接口，用户提交分成两步，第一步提交申请表单数据，第二部提交转账凭证")
    @PostMapping("doc/api/putDeposit")
    @ResponseBody
    public R putDeposit(@Valid DepRequest dep) {
        return R.failed("入金失败") ;
    }
    @ApiOperation(value = "入金申请列表查询，带分页数据")
    @PostMapping("doc/api/depositList")
    @ResponseBody
    public R<Page<DepRequest>> depositList(@Valid DepositQuery<DepRequest> query) {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "入金申请详情")
    @GetMapping("doc/api/depositLGet")
    @ResponseBody
    public R<DepRequest> depositLGet(Long id) {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "入金审核（接口会判断admin权限）审核由财务审核，财务审核完成后就确认入账")
    @PostMapping("doc/api/perDeposit")
    @RequiresRoles("admin")
    @ResponseBody
    public R perDeposit(@Valid PerReqVO per) {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "加密货币入金申请提交接口")
    @PostMapping("doc/api/putCryptDeposit")
    @ResponseBody
    public R putCryptDeposit(@Valid CryptRequest req) {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "加密货币入金列表查询，带分页")
    @PostMapping("doc/api/cryptDepositList")
    @ResponseBody
    public R<Page<CryptRequest>> cryptDepositList(@Valid CryQuery<CryptRequest> query) {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "加密货币入金详情")
    @GetMapping("doc/api/cryptDepositGet")
    @ResponseBody
    public R<CryptRequest> cryptDepositGet(Long id) {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "入金审核（admin）")
    @PostMapping("doc/api/perCryptDeposit")
    @RequiresRoles("admin")
    @ResponseBody
    public R perCryptDeposit(@Valid PerReqVO per) {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "出金申请withdraw")
    @PostMapping("doc/api/putWithdraw")
    @ResponseBody
    public R putWithdraw(@Valid WithdrawRequest withdraw) {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "出金列表查询，带分页")
    @PostMapping("doc/api/withdrawList")
    @ResponseBody
    public R<Page<WithdrawRequest>> withdrawList(@Valid WithdrawQuery<WithdrawRequest> query) {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "出金审核（admin）--> 审核分为两道，第一次审核，状态是1时，审核通过状态变为2，当状态是2时，审核需要填写实际出款金额和手续费以及上传银行汇款水单")
    @PostMapping("doc/api/perWithdraw")
    @RequiresRoles("admin")
    @ResponseBody
    public R perWithdraw(@Valid PerReqVO per) {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "加密货币出金申请withdraw")
    @PostMapping("doc/api/putCryptWithdraw")
    @ResponseBody
    public R putCryptWithdraw(@Valid CryptRequest req) {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "加密货币出金列表查询，带分页")
    @PostMapping("doc/api/cryptWithdrawList")
    @ResponseBody
    public R<Page<CryptRequest>> cryptWithdrawList(@Valid CryQuery<CryptRequest> query) {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "加密货币出金详情")
    @GetMapping("doc/api/cryptWithdrawGet")
    @ResponseBody
    public R<CryptRequest> cryptWithdrawGet(Long id) {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "加密货币出金审核（admin）--> 状态是1时，审核直接就是已转出，需要提交钱包转账截图")
    @PostMapping("doc/api/perCryptWithdraw")
    @RequiresRoles("admin")
    @ResponseBody
    public R perCryptWithdraw(@Valid PerReqVO per) {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "可选择入金货币")
    @GetMapping("doc/api/depCoins")
    @ResponseBody
    public R<List<Deposit>> depCoins() {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "可选择出金账本列表")
    @GetMapping("doc/api/balanceList")
    @ResponseBody
    public R<List<BalanceDetail>> balanceList() {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "可选择选择出金币种")
    @GetMapping("doc/api/withdrawCoins")
    @ResponseBody
    public R<List<String>> withdrawCoins(String code) {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "选择出金账户列表")
    @GetMapping("doc/api/withdrawAccounts")
    @ResponseBody
    public R<List<BankDetail>> withdrawAccounts() {
        return R.failed("入金失败") ;
    }

    @ApiOperation(value = "传入来源货币代码，提现金额，目标货币代码，会计算出预计目标货币金额")
    @PostMapping("doc/api/calculateRate")
    @ResponseBody
    public R<ExQuery> calculateRate(@Valid ExQuery change) {
        return R.failed("入金失败") ;
    }
}
