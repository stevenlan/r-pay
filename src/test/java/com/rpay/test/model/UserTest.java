package com.rpay.test.model;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rpay.FreeFsApplication;
import com.rpay.mapper.*;
import com.rpay.model.Countries;
import com.rpay.model.Exchange;
import com.rpay.model.User;
import com.rpay.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {FreeFsApplication.class},properties = {"spring.profiles.active=dev"})
@AutoConfigureMockMvc
public class UserTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()) ;

    @Autowired
    private UserMapper userMapper ;
    @Autowired
    private UserService userService ;

    @Test
    public void testQueryUser() {
        User user = userMapper.selectById(1) ;
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getId().toString(),"1");
    }

    @Autowired
    private BankDetailMapper bankMapper ;
    @Autowired
    private KycCertificationMapper kycMapper ;

//    @Test
//    public void testQuery() {
//        Integer n = bankMapper.selectCount(new QueryWrapper<>()) ;
//        Assert.assertNotNull(n);
//        Assert.assertEquals(1,n.intValue()) ;
//
//        Integer c = kycMapper.selectCount(new QueryWrapper<>()) ;
//        Assert.assertNotNull(c);
//        Assert.assertEquals(0,c.intValue()) ;
//    }

    @Autowired
    private CountriesMapper countriesMapper ;

    @Test
    public void testCounties() {
        Integer n = countriesMapper.selectCount(new QueryWrapper<>()) ;
        Assert.assertNotNull(n);
        Assert.assertEquals(246,n.intValue()) ;

        Countries c = countriesMapper.selectOne(new QueryWrapper<Countries>().lambda().eq(Countries::getCode,"CN")) ;
        Assert.assertNotNull(c);
        Assert.assertEquals(c.getAreaCode(),"+86");
    }

    @Autowired
    private ExchangeMapper exchangeMapper ;
    @Test
    public void testEx() {
        Exchange e = new Exchange() ;
//        e.setExFrom("CHINA") ;
//        e.setExTarget("USDT") ;
//        e.setExRate(0.15F) ;
//
//        exchangeMapper.insert(e) ;
//
//        Exchange e1 = exchangeMapper.selectOne(new QueryWrapper<Exchange>().lambda().eq(Exchange::getExFrom,e.getExFrom())) ;
//        Assert.assertNotNull(e1) ;
//
//        exchangeMapper.deleteById(e1.getId()) ;
    }

    @Test
    public void userAdmin() {
        User user = new User() ;
        user.setId(1L);
        Assert.assertTrue(userService.isAdmin(user)) ;
    }
}
