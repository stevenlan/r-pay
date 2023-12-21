package com.rpay.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.common.utils.R;
import com.rpay.model.CoinLimit;
import com.rpay.service.LimitServices;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author steven
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class LimitController {
    private final LimitServices limitServices ;

    @ApiOperation(value = "获取对应货币的不同操作的限额")
    @GetMapping("/api/limit/getLimit")
    @ResponseBody
    public R<CoinLimit> getLimit(String coin, String act) {
        CoinLimit limit = limitServices.limitGet(coin,act) ;
        return R.succeed(limit) ;
    }

    @ApiOperation(value = "获取对应货币限额的分页信息")
    @PostMapping("/api/limit/pageLimit")
    @ResponseBody
    public R<Page<CoinLimit>> pageLimit(Page<CoinLimit> query) {
        return R.succeed(limitServices.limitPage(query)) ;
    }
}
