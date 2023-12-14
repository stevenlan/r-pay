package com.rpay.test.services;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.FreeFsApplication;
import com.rpay.mapper.DepositMapper;
import com.rpay.model.*;
import com.rpay.service.AccountService;
import com.rpay.service.ExchangeService;
import com.rpay.service.query.KycQuery;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {FreeFsApplication.class},properties = {"spring.profiles.active=dev"})
@AutoConfigureMockMvc
public class AccountTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()) ;

    @Autowired
    private AccountService accountService ;

    @Autowired
    private ExchangeService exService ;

    @Autowired
    private DepositMapper depMapper ;

    @Test
    public void testInit() {
        List<Countries> countries = accountService.getCountries() ;
        Assert.assertNotNull(countries) ;
        Assert.assertTrue(countries.size() > 0);

        List<Countries> crypts = accountService.getCrypts() ;
        Assert.assertNotNull(countries) ;
        Assert.assertTrue(countries.size() > 0);
    }

    @Test
    public void testBank() {
        //exService.exchanges() ;
        List<Deposit> depList = depMapper.queryList() ;
        Assert.assertNotNull(depList) ;
    }

    @Test
    public void dataInit() {
        //Page<KycCertification> page = accountService.pageKycList(new KycQuery()) ;
        //CryAccount acc = exService.findCry("USDT",2L) ;
        //Assert.assertNotNull(acc);
        //创建账号
        //提交企业信息以及资质
        //提交银行账户信息以及资质证明
        //审批企业资质
        //审批银行账户资料
        //设置汇率
        //设置收款银行账户
        //完成初始化
    }
}
