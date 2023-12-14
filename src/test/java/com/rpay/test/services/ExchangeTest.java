package com.rpay.test.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.FreeFsApplication;
import com.rpay.mapper.CountriesMapper;
import com.rpay.mapper.ExchangeMapper;
import com.rpay.model.Countries;
import com.rpay.model.bill.ChangeDetail;
import com.rpay.service.BillService;
import com.rpay.service.UserService;
import com.rpay.service.i18n.LocaleMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {FreeFsApplication.class},properties = {"spring.profiles.active=dev"})
@AutoConfigureMockMvc
@Slf4j
public class ExchangeTest {
    @Autowired
    private ExchangeMapper exMapper ;

    @Autowired
    private CountriesMapper countriesMapper ;

    @Autowired
    private LocaleMessageUtil messageSource;

    @Autowired
    private BillService billService ;

    @Autowired
    private UserService userService ;

    @Test
    public void initEx() {
        List<String> l = exMapper.withdrawCoinsForType("CNY") ;
        Assert.assertNotNull(l) ;

        List<Countries> countries = countriesMapper.selectExSource() ;
        Assert.assertNotNull(countries) ;

        String m = messageSource.getMessage("broker.title") ;
        Assert.assertNotNull(m);
    }

    @Test
    public void testUser(){
        userService.inviteUser("TOVyrsqL",new Page<>()) ;
    }

    /*@Test
    public void testChange() {
        ChangeDetail change = new ChangeDetail() ;
        change.setDepCoin("ARS");
        change.setDepValue(11.0);
        change.setTargetCoin("AED");
        change.setChangeStatus(0) ;
        change.setUserId(2L);

        billService.creChange(change) ;
    }*/
}
