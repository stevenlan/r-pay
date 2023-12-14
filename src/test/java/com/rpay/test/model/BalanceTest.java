package com.rpay.test.model;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rpay.FreeFsApplication;
import com.rpay.mapper.CryptRequestMapper;
import com.rpay.mapper.bill.BalanceDetailMapper;
import com.rpay.mapper.bill.BillDetailMapper;
import com.rpay.mapper.bill.ChangeDetailMapper;
import com.rpay.mapper.deposit.DepRequestMapper;
import com.rpay.mapper.withdraw.WithdrawRequestMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {FreeFsApplication.class},properties = {"spring.profiles.active=dev"})
@AutoConfigureMockMvc
public class BalanceTest {
    @Autowired
    private DepRequestMapper depRepMapper ;
    @Autowired
    private WithdrawRequestMapper withReqMapper ;
    @Autowired
    private CryptRequestMapper cryReqMapper ;
    @Autowired
    private BalanceDetailMapper balMapper ;
    @Autowired
    private BillDetailMapper billMapper ;
    @Autowired
    private ChangeDetailMapper changeMapper ;

    @Test
    public void test() {
        //Integer count = depRepMapper.selectCount(new QueryWrapper<>()) ;
        //Integer count = withReqMapper.selectCount(new QueryWrapper<>()) ;
        Integer count = cryReqMapper.selectCount(new QueryWrapper<>()) ;
        Assert.assertNotNull(count) ;

        Integer count1 = balMapper.selectCount(new QueryWrapper<>()) ;
        Assert.assertNotNull(count1) ;

        Integer count2 = billMapper.selectCount(new QueryWrapper<>()) ;
        Assert.assertNotNull(count2) ;

        Integer count3 = changeMapper.selectCount(new QueryWrapper<>()) ;
        Assert.assertNotNull(count3) ;
    }
}
