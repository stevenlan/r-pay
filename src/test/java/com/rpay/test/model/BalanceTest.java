package com.rpay.test.model;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.FreeFsApplication;
import com.rpay.common.exception.BusinessException;
import com.rpay.common.utils.LimitEnum;
import com.rpay.mapper.CryptRequestMapper;
import com.rpay.mapper.bill.BalanceDetailMapper;
import com.rpay.mapper.bill.BillDetailMapper;
import com.rpay.mapper.bill.ChangeDetailMapper;
import com.rpay.mapper.deposit.DepRequestMapper;
import com.rpay.mapper.withdraw.WithdrawRequestMapper;
import com.rpay.model.BankDetail;
import com.rpay.model.CryAccount;
import com.rpay.model.validate.group.AdminGroup;
import com.rpay.model.validate.group.UserGroup;
import com.rpay.service.LimitServices;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.function.ThrowingRunnable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import java.util.Set;

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
    @Autowired
    private LimitServices limitServices ;

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

    //@Test
    public void testLimit() {
        limitServices.limitPage(new Page<>()) ;
        limitServices.limitGet("DZD", LimitEnum.Deposit.getActType()) ;

        Assert.assertThrows(BusinessException.class,new ThrowingRunnable(){

            @Override
            public void run() throws Throwable {
                limitServices.calculateLimit("DZD",LimitEnum.Deposit.getActType(),50D) ;
            }
        }) ;
    }

    @Autowired
    private SmartValidator validator ;

    @Test
    public void testValid() {
        CryAccount acc = new CryAccount() ;
        BindException bindException = new BindException(acc, CryAccount.class.getName());
        validator.validate(acc,bindException, UserGroup.class) ;
        Assert.assertTrue(bindException.hasErrors());
    }
}
