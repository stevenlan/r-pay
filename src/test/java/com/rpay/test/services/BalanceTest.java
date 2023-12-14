package com.rpay.test.services;

import com.rpay.FreeFsApplication;
import com.rpay.model.Exchange;
import com.rpay.model.bill.BillDetail;
import com.rpay.model.deposit.DepRequest;
import com.rpay.service.AccountService;
import com.rpay.service.BalanceService;
import com.rpay.service.BillService;
import com.rpay.service.query.DepositQuery;
import com.rpay.service.query.ExQuery;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {FreeFsApplication.class},properties = {"spring.profiles.active=dev"})
@AutoConfigureMockMvc
@Slf4j
public class BalanceTest {
    @Autowired
    private BalanceService balService ;
    @Autowired
    private BillService billService ;
    @Autowired
    private AccountService accService ;

    @Test
    public void initBal() {
//        DepRequest dep = new DepRequest() ;
//        dep.setId(2L) ;
//        dep.setReqProof("group1/M00/00/00/rBEn2mU7zhOASPRBAABuYzvvTkk423.pdf") ;
//        balService.putDepReq(dep) ;
        ExQuery change = new ExQuery() ;
        change.setExFrom("DZD") ;
        change.setExTarget("USDT") ;
        change.setExValue(1.0);
        Exchange ex = accService.matchExchange(change) ;
        //balService.withdrawCoins(null) ;
        //创建入账申请单
        //提交转账资料流水单据
        //审核到账
        //查询账户余额
        //查询可以提款列表
        //提交提款申请单
        //确认提款申请
        //提交转账单据，转账完成
    }
}
