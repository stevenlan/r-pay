package com.rpay.controller.docs;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.common.utils.R;
import com.rpay.common.utils.SessionUtils;
import com.rpay.controller.BaseController;
import com.rpay.controller.vo.PerChange;
import com.rpay.model.Countries;
import com.rpay.model.bill.ChangeDetail;
import com.rpay.service.BillService;
import com.rpay.service.ExchangeService;
import com.rpay.service.query.ChangeDetailQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @author steven
 */
@Controller
@RequiredArgsConstructor
public class DocChangeController extends BaseController implements SessionUtils {
    private final ExchangeService exService ;
    private final BillService billService ;

    @ApiOperation(value = "查询可兑换来源列表")
    @GetMapping("doc/api/sourceCoin")
    @ResponseBody
    public R<List<Countries>> sourceCoin(){
        return R.succeed(exService.queryExSource()) ;
    }

    @ApiOperation(value = "查询可兑换目标列表")
    @GetMapping("doc/api/targetCoin")
    @ResponseBody
    public R<List<Countries>> targetCoin(@ApiParam(name = "source", value = "来源货币国家，可以是法币，也可以是数据货币,提交的是地区列表的code, 比如CN / US", required = true) String source){
        return R.succeed(exService.queryExTarget(source)) ;
    }

    //status:1
    @ApiOperation(value = "汇兑详情列表查询")
    @PostMapping("doc/api/changeDetails")
    @ResponseBody
    public R<Page<ChangeDetail>> changeDetails(ChangeDetailQuery query) {
        return R.succeed(billService.changeDetails(query)) ;
    }

    @ApiOperation(value = "提交汇款详情单")
    @PostMapping("doc/api/changeReq")
    @ResponseBody
    public R changeReq(ChangeDetail change) {
        change.setChangeStatus(0) ;
        billService.recordChange(change) ;
        return R.succeed("操作成功") ;
    }

    @ApiOperation(value = "汇兑详情获取")
    @GetMapping("doc/api/changeGet")
    @ResponseBody
    public R<ChangeDetail> changeGet(Long id) {
        return R.succeed(billService.getChangeDetail(id)) ;
    }

    @ApiOperation(value = "取消汇兑详情")
    @GetMapping("doc/api/changeCancel")
    @ResponseBody
    public R changeCancel(Long id) {
        billService.cancelChange(id) ;
        return R.succeed("操作成功") ;
    }

    @ApiOperation(value = "审核汇款详情单")
    @PostMapping("doc/api/changePer")
    @ResponseBody
    public R changePer(PerChange per) {
        billService.perChange(per.getId(),per.getPass(),per.getMime()) ;
        return R.succeed("操作成功") ;
    }
}
