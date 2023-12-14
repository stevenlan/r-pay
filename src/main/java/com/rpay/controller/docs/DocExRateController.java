package com.rpay.controller.docs;

import com.rpay.common.utils.R;
import com.rpay.model.BankDetail;
import com.rpay.model.CryAccount;
import com.rpay.model.Deposit;
import com.rpay.model.Exchange;
import com.rpay.service.AccountService;
import com.rpay.service.ExchangeService;
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
 * 汇率固定设置兑USDT和美金的交易对
 * @author steven
 */
@Controller
@RequiredArgsConstructor
public class DocExRateController {
    private final ExchangeService exService ;
    private final AccountService accService ;

    @ApiOperation(value = "设置汇率对的汇率价格")
    @PostMapping("doc/api/setExchange")
    @RequiresRoles("admin")
    @ResponseBody
    public R setExchange(@Valid Exchange ex) {
        return R.failed("操作失败") ;
    }

    @ApiOperation(value = "获取所支持的汇率对价格列表")
    @GetMapping("doc/api/getExchanges")
    @ResponseBody
    public R<List<Exchange>> getExchanges() {
        return R.failed("操作失败") ;
    }

    @ApiOperation(value = "删除汇率对")
    @GetMapping("doc/api/delExchange")
    @RequiresRoles("admin")
    @ResponseBody
    public R delExchange(Long id) {
        return R.failed("操作失败") ;
    }

    @ApiOperation(value = "设置目前支持的收款户货币银行账户")
    @PostMapping("doc/api/setDeposit")
    @RequiresRoles("admin")
    @ResponseBody
    public R setDeposit(@Valid BankDetail deposit) {
        return R.failed("操作失败") ;
    }

    @ApiOperation(value = "获取平台收款户货币银行账户")
    @GetMapping("doc/api/getDeposits")
    @ResponseBody
    public R<List<Deposit>> getDeposits() {
        return R.failed("操作失败") ;
    }

    @ApiOperation(value = "删除收款账户")
    @GetMapping("doc/api/delDeposit")
    @RequiresRoles("admin")
    @ResponseBody
    public R delDeposit(Long id) {
        return R.failed("操作失败") ;
    }

    @ApiOperation(value = "给认证完成的用户设置加密钱包地址，修改和新增都是用同一个接口")
    @PostMapping("doc/api/setCryAcc")
    @RequiresRoles("admin")
    @ResponseBody
    public R setCryAcc(@Valid CryAccount crt) {
        return R.failed("操作失败") ;
    }
    //收款管理，汇兑管理，付款管理
}
