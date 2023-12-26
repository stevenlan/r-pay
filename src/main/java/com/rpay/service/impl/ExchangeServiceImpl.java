package com.rpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.common.exception.BusinessException;
import com.rpay.common.utils.SessionUtils;
import com.rpay.mapper.CountriesMapper;
import com.rpay.mapper.CryAccountMapper;
import com.rpay.mapper.DepositMapper;
import com.rpay.mapper.ExchangeMapper;
import com.rpay.model.*;
import com.rpay.service.AccountService;
import com.rpay.service.ExchangeService;
import com.rpay.service.UserService;
import com.rpay.service.query.CryAccQuery;
import com.rpay.service.query.ExQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author steven
 */
@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService, SessionUtils {
    private final Logger log = LoggerFactory.getLogger(this.getClass()) ;

    private final ExchangeMapper exMapper ;
    private final DepositMapper depMapper ;
    private final CryAccountMapper cryMapper ;
    private final AccountService accService ;
    private final CountriesMapper countriesMapper ;
    private final UserService userService ;

    @Override
    public boolean updateExchange(Exchange exchange) {
        if ( null == exchange.getId() ) {
            return exMapper.insert(exchange) > 0 ;
        }
        return exMapper.updateById(exchange) >0 ;
    }

    @Override
    public List<Exchange> exchanges() {
        return exMapper.selectList(new QueryWrapper<Exchange>().lambda().orderByDesc(Exchange::getModifiedTime)) ;
    }

    @Override
    public Page<Exchange> exchangesPage(Page<Exchange> query) {
        return exMapper.selectPage(query,new QueryWrapper<Exchange>().lambda().orderByDesc(Exchange::getModifiedTime)) ;
    }

    @Override
    public boolean delExchange(Long id) {
        return exMapper.deleteById(id) > 0 ;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean updateDeposit(BankDetail dep) {
        Deposit tmp = depMapper.selectOne(new QueryWrapper<Deposit>().lambda()
                .eq(Deposit::getId,dep.getId()).or().eq(Deposit::getCoinCode,dep.getCoinCode())) ;

        if ( null == tmp ) {
            Long userId = getLoginUserId() ;
            dep.setBankStatus(1) ;
            accService.updateAccount(dep) ;
            Deposit deposit = new Deposit() ;
            deposit.setCoinCode(dep.getCoinCode());
            deposit.setDepositStatus(1) ;
            deposit.setBankId(dep.getId()) ;
            deposit.setUserId(userId) ;
            return depMapper.insert(deposit) > 0 ;
        } else {
            Deposit deposit = new Deposit() ;
            deposit.setId(dep.getId()) ;
            deposit.setCoinCode(dep.getCoinCode());
            deposit.setDepositStatus(1) ;
            deposit.setBankId(tmp.getBankId()) ;

            dep.setId(tmp.getBankId()) ;
            dep.setBankStatus(1) ;
            accService.updateAccount(dep) ;
            return depMapper.updateById(deposit) > 0;
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void bindCoins(List<String> coins, Long bankId) {
        delBindCoins(bankId) ;
        Long userId = getLoginUserId() ;
        coins.forEach(coin -> {
            Deposit deposit = new Deposit() ;
            deposit.setCoinCode(coin);
            deposit.setDepositStatus(1) ;
            deposit.setBankId(bankId) ;
            deposit.setUserId(userId) ;

            depMapper.insert(deposit) ;
        });
    }

    @Override
    public void delBindCoins(Long bankId) {
        depMapper.delete(new QueryWrapper<Deposit>().lambda().eq(Deposit::getBankId,bankId)) ;
    }

    @Override
    public List<Deposit> deposits() {
        return depMapper.queryList() ;
    }

    @Override
    public List<Deposit> coinBankIds(String coin) {
        Long userId = getLoginUserId() ;
        return depMapper.selectList(new QueryWrapper<Deposit>().lambda().eq(Deposit::getUserId,userId)
                .eq(Deposit::getCoinCode, coin)) ;
    }

    @Override
    public List<Deposit> bankCoins(Long bankId) {
        Long userId = getLoginUserId() ;
        return depMapper.selectList(new QueryWrapper<Deposit>().lambda().eq(Deposit::getUserId,userId)
                .eq(Deposit::getBankId, bankId)) ;
    }

    @Override
    public Page<Deposit> depositPage(Page<Deposit> query) {
        Integer count = depMapper.countQueryList() ;
        query.setTotal(count) ;

        if ( count > 0 ) {
            List<Deposit> list = depMapper.queryList(query.getCurrent(),query.getSize()) ;
            query.setRecords(list) ;
        }

        return query ;
    }

    @Override
    public boolean delDeposit(Long id) {
        Deposit dep = depMapper.selectById(id) ;
        if ( depMapper.deleteById(id) > 0 ) {
            BankDetail bank = accService.findBank(dep.getBankId()) ;
            accService.bankDel(bank, null);
            return true ;
        }
        return false ;
    }

    @Override
    public boolean updateCry(CryAccount cryAcc) {
        CryAccount tmp = cryMapper.selectOne(new QueryWrapper<CryAccount>().lambda()
                .eq(CryAccount::getCryType,0)
                .eq(CryAccount::getAgreement,cryAcc.getAgreement())
                .eq(CryAccount::getUserId,cryAcc.getUserId())) ;
        if ( null == tmp ) {
            cryAcc.setId(null);
            return cryMapper.insert(cryAcc) > 0 ;
        }
        cryAcc.setId(tmp.getId()) ;
        return cryMapper.updateById(cryAcc) > 0;
    }

    @Override
    public boolean updateOutCry(CryAccount cryAcc) {
        if ( StringUtils.isNotBlank(cryAcc.getPayPass()) ) {
            //验证支付密码
            boolean flag = userService.checkPayPass(getLoginUserId(), cryAcc.getPayPass()) ;
            if ( !flag ) {
                //输入错误计数
                throw new BusinessException("{sys.pay.err}") ;
            }
        }
        if ( null == cryAcc.getId() ) {
            return cryMapper.insert(cryAcc) > 0 ;
        }
        return cryMapper.updateById(cryAcc) > 0;
    }

    @Override
    public List<CryAccount> findCry(String cryCode, Long userId) {
        return cryMapper.selectList(new QueryWrapper<CryAccount>().lambda().eq(CryAccount::getCryCode,cryCode)
                .eq(CryAccount::getUserId,userId)) ;
    }

    @Override
    public Page<CryAccount> pageCryAcc(CryAccQuery query) {
        QueryWrapper<CryAccount> q = new QueryWrapper<>() ;
        q.lambda().eq(CryAccount::getCryType, 1)
                .eq(StringUtils.isNotBlank(query.getCryCode()),CryAccount::getCryCode,query.getCryCode())
                .eq(CryAccount::getUserId,getLoginUserId())
                .orderByDesc(CryAccount::getModifiedTime) ;

        return cryMapper.selectPage(query, q) ;
    }

    @Override
    public List<CryAccount> listCryAcc(String agreement, String cryCode) {
        QueryWrapper<CryAccount> q = new QueryWrapper<>() ;
        q.lambda().eq(CryAccount::getCryType, 1)
                .eq(CryAccount::getAgreement,agreement)
                .eq(CryAccount::getCryCode,cryCode)
                .eq(CryAccount::getUserId,getLoginUserId())
                .orderByDesc(CryAccount::getModifiedTime) ;
        return cryMapper.selectList(q) ;
    }

    @Override
    public List<Countries> queryExSource() {
        return countriesMapper.selectExSource() ;
    }

    @Override
    public List<Countries> queryExTarget(String fromCountry) {
        return countriesMapper.selectExTarget(fromCountry) ;
    }

    @Override
    public Set<String> queryAllExCoin() {
        Set<String> coins = new HashSet<>() ;
        List<Countries> sources = queryExSource() ;
        if (!ListUtils.isEmpty(sources)) {
            sources.forEach(countries -> {
                if ( countries.getCoinType() == 1 && StringUtils.isNotBlank(countries.getCoinCode())) {
                    coins.add(countries.getCoinCode());
                }
            });
        }
        List<Countries> targets = countriesMapper.selectAllExTarget() ;
        if (!ListUtils.isEmpty(targets)) {
            targets.forEach(countries -> {
                if ( countries.getCoinType() == 1 && StringUtils.isNotBlank(countries.getCoinCode()) ) {
                    coins.add(countries.getCoinCode());
                }
            });
        }

        return coins ;
    }
}
