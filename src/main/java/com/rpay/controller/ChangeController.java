package com.rpay.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.common.utils.R;
import com.rpay.common.utils.SessionUtils;
import com.rpay.controller.vo.PerChange;
import com.rpay.model.Countries;
import com.rpay.model.bill.ChangeDetail;
import com.rpay.service.BillService;
import com.rpay.service.ExchangeService;
import com.rpay.service.LimitServices;
import com.rpay.service.query.ChangeDetailQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @author steven
 */
@Controller
@RequiredArgsConstructor
public class ChangeController extends BaseController implements SessionUtils {
    private final ExchangeService exService ;
    private final BillService billService ;
    private final LimitServices limitServices ;

    @ApiOperation(value = "查询可兑换来源列表")
    @GetMapping("/api/sourceCoin")
    @ResponseBody
    public R<List<Countries>> sourceCoin(){
        return R.succeed(exService.queryExSource()) ;
    }

    @ApiOperation(value = "查询可兑换目标列表")
    @GetMapping("/api/targetCoin")
    @ResponseBody
    public R<List<Countries>> targetCoin(@ApiParam(name = "source", value = "来源货币国家，可以是法币，也可以是数据货币,提交的是地区列表的code, 比如CN / US", required = true) String source){
        return R.succeed(exService.queryExTarget(source)) ;
    }

    @ApiOperation(value = "查询可兑换目标列表")
    @GetMapping("/api/allCoins")
    @ResponseBody
    public R<Set<String>> allCoins(){
        return R.succeed(exService.queryAllExCoin()) ;
    }

    //status:1
    @ApiOperation(value = "汇兑详情列表查询")
    @PostMapping("/api/changeDetails")
    @ResponseBody
    public R<Page<ChangeDetail>> changeDetails(@Valid @RequestBody ChangeDetailQuery query) {
        return R.succeed(billService.changeDetails(query)) ;
    }

    @ApiOperation(value = "提交汇款详情单")
    @PostMapping("/api/changeReq")
    @ResponseBody
    public R changeReq(@Valid @RequestBody ChangeDetail change) {
        change.setChangeStatus(0) ;
        change.setUserId(getLoginUserId()) ;
        //计算限制
        billService.creChange(change) ;
        return R.succeed("操作成功") ;
    }

    @ApiOperation(value = "汇兑详情获取")
    @GetMapping("/api/changeGet")
    @ResponseBody
    public R<ChangeDetail> changeGet(Long id) {
        return R.succeed(billService.getChangeDetail(id)) ;
    }

    @ApiOperation(value = "取消汇兑详情")
    @GetMapping("/api/changeCancel")
    @ResponseBody
    public R changeCancel(Long id) {
        billService.cancelChange(id) ;
        return R.succeed("操作成功") ;
    }

    @ApiOperation(value = "审核汇款详情单")
    @PostMapping("/api/changePer")
    @RequiresRoles("admin")
    @ResponseBody
    public R changePer(@Valid @RequestBody PerChange per) {
        billService.perChange(per.getId(),per.getPass(),per.getMime()) ;
        return R.succeed("操作成功") ;
    }
}
