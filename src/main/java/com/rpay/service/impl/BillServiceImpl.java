package com.rpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.common.exception.BusinessException;
import com.rpay.common.utils.SessionUtils;
import com.rpay.mapper.CountriesMapper;
import com.rpay.mapper.bill.BalanceDetailMapper;
import com.rpay.mapper.bill.BillDetailMapper;
import com.rpay.mapper.bill.ChangeDetailMapper;
import com.rpay.model.Countries;
import com.rpay.model.Exchange;
import com.rpay.model.User;
import com.rpay.model.bill.BalanceDetail;
import com.rpay.model.bill.BillDetail;
import com.rpay.model.bill.ChangeDetail;
import com.rpay.service.AccountService;
import com.rpay.service.BillService;
import com.rpay.service.UserService;
import com.rpay.service.query.BillDetailQuery;
import com.rpay.service.query.ChangeDetailQuery;
import com.rpay.service.query.ExQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author steven
 */
@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService, SessionUtils {
    private final BillDetailMapper billMapper ;
    private final BalanceDetailMapper balanceMapper ;
    private final ChangeDetailMapper changeMapper ;
    private final CountriesMapper countriesMapper ;
    private final UserService userService ;
    private final AccountService accountService ;

    @Override
    public boolean recordDepBill(BillDetail bill) {
        BalanceDetail detail = balanceMapper.selectOne(new QueryWrapper<BalanceDetail>().lambda()
                .eq(BalanceDetail::getUserId, bill.getUserId())
                .eq(BalanceDetail::getCoinCode,bill.getCoinCode())) ;

        //判断余额是否充足
        if ( 1 != bill.getBillType() && 4 != bill.getBillType() ) {
            if ( null == detail || detail.getBalance() < bill.getBillValue()  ) {
                throw new BusinessException("余额不足，请刷新余额后再操作");
            }
        }

        List<Countries> countries = countriesMapper.selectList(new QueryWrapper<Countries>().lambda()
                .eq(Countries::getCoinCode,bill.getCoinCode())) ;
        //判断所提货币存在于否
        if ( null == countries && countries.isEmpty() ) {
            throw new BusinessException("所选择货币不存在") ;
        }

        bill.setOuterType(countries.get(0).getCoinType()) ;
        if ( null == detail && ( 1 == bill.getBillType() || 4 == bill.getBillType() ) ) {
            detail = new BalanceDetail() ;
            detail.setCoinCode(bill.getCoinCode()) ;
            detail.setBalance(bill.getBillValue()) ;
            detail.setUserId(bill.getUserId()) ;
            detail.setBalType(bill.getOuterType()) ;

            bill.setBalance(bill.getBillValue()) ;

            balanceMapper.insert(detail) ;
        } else if (null != detail) {
            LambdaUpdateChainWrapper<BalanceDetail> up = new LambdaUpdateChainWrapper<>(balanceMapper) ;
            up.eq(BalanceDetail::getUserId, bill.getUserId())
                    .eq(BalanceDetail::getCoinCode,bill.getCoinCode()) ;
            if ( 1 == bill.getBillType() || 4 == bill.getBillType() ) {
                up.setSql("balance = balance + "+ bill.getBillValue() +"") ;
            } else {
                up.setSql("balance = balance - "+ bill.getBillValue() +"") ;
                //up.ge(BalanceDetail::getBalance, bill.getBillValue()) ;
            }
            up.update() ;

            detail = balanceMapper.selectById(detail.getId()) ;
            bill.setBalance(detail.getBalance()) ;
        } else {
            throw new BusinessException("操作异常，请重新再试") ;
        }

        return billMapper.insert(bill) > 0;
    }

    @Override
    public boolean recordChange(ChangeDetail change) {
        return changeMapper.insert(change) > 0 ;
    }

    @Override
    public Page<BillDetail> balDetails(BillDetailQuery query) {
        QueryWrapper<BillDetail> que = new QueryWrapper<>() ;
        boolean admin = userService.isAdmin(getLoginUser()) ;
        if (StringUtils.isNotBlank(query.getEmail()) && admin) {
            User user = userService.getOne(new QueryWrapper<User>().lambda().eq(User::getEmail, query.getEmail()));
            if (null == user) {
                return new Page<>();
            }
            que.lambda().eq(BillDetail::getUserId, user.getId()) ;
        } else if ( !admin ) {
            que.lambda().eq(BillDetail::getUserId, getLoginUserId()) ;
        }
        que.lambda().eq(StringUtils.isNotBlank(query.getCoinCode()), BillDetail::getCoinCode,query.getCoinCode())
                .eq(null!=query.getBizType(),BillDetail::getBillType, query.getBizType())
                .gt(null != query.getStartTime(), BillDetail::getCreateTime, query.getStartTime())
                .lt(null != query.getEndTime(), BillDetail::getCreateTime, query.getEndTime())
                .orderByDesc(BillDetail::getModifiedTime) ;

        return billMapper.selectPage(query,que) ;
    }

    @Override
    public Page<ChangeDetail> changeDetails(ChangeDetailQuery query) {
        QueryWrapper<ChangeDetail> que = new QueryWrapper<>() ;
        boolean admin = userService.isAdmin(getLoginUser()) ;
        if (StringUtils.isNotBlank(query.getEmail()) && admin) {
            User user = userService.getOne(new QueryWrapper<User>().lambda().eq(User::getEmail, query.getEmail()));
            if (null == user) {
                return new Page<>();
            }
            que.lambda().eq(ChangeDetail::getUserId, user.getId()) ;
        } else if ( !admin ){
            que.lambda().eq(ChangeDetail::getUserId, getLoginUserId()) ;
        }
        que.lambda().eq(StringUtils.isNotBlank(query.getFromCode()), ChangeDetail::getDepCoin,query.getFromCode())
                .eq(null != query.getChangeStatus(), ChangeDetail::getChangeStatus, query.getChangeStatus())
                .eq(StringUtils.isNotBlank(query.getTargetCode()), ChangeDetail::getTargetCoin,query.getTargetCode())
                .gt(null != query.getStartTime(), ChangeDetail::getCreateTime, query.getStartTime())
                .lt(null != query.getEndTime(), ChangeDetail::getCreateTime, query.getEndTime())
                .orderByDesc(ChangeDetail::getModifiedTime) ;

        return changeMapper.selectPage(query,que) ;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean creChange(ChangeDetail req) {
        ExQuery q = new ExQuery() ;
        q.setExFrom(req.getDepCoin()) ;
        q.setExTarget(req.getTargetCoin());
        Exchange change = accountService.matchExchange(q) ;
        if ( null == change ) {
            throw new BusinessException("当前不支持该种货币兑换") ;
        }

        req.setTargetValue(comChange(change, req.getDepValue())) ;
        req.setChangeRate(change.getExRate()) ;
        recordChange(req) ;

        BillDetail bill = new BillDetail() ;
        bill.setBillType(3) ;
        bill.setUserId(req.getUserId()) ;
        bill.setBillValue(req.getDepValue()) ;
        bill.setCoinCode(req.getDepCoin()) ;
        bill.setOuterId(req.getId()) ;

        return recordDepBill(bill) ;
    }

    @Override
    public boolean cancelChange(Long id) {
        QueryWrapper<ChangeDetail> query = new QueryWrapper<>() ;
        query.lambda().eq(ChangeDetail::getId,id).eq(ChangeDetail::getUserId,getLoginUserId())
                .eq(ChangeDetail::getChangeStatus, 0) ;
        ChangeDetail change = changeMapper.selectOne(query) ;
        if ( null == change ) {
            throw new BusinessException("不存在该任务，或者不允许取消") ;
        }
        LambdaUpdateChainWrapper<ChangeDetail> up = new LambdaUpdateChainWrapper<>(changeMapper) ;
        up.eq(ChangeDetail::getId,id) ;
        up.set(ChangeDetail::getChangeStatus,2) ;

        //回写账本金额
        rollbackBal(change.getUserId(),change.getDepCoin(),3,change.getId(),4) ;
        return up.update() ;
    }

    @Override
    public boolean perChange(Long id, boolean pass, String mime) {
        QueryWrapper<ChangeDetail> query = new QueryWrapper<>() ;
        query.lambda().eq(ChangeDetail::getId,id)
                .eq(ChangeDetail::getChangeStatus, 0) ;
        ChangeDetail change = changeMapper.selectOne(query) ;
        if ( null == change ) {
            throw new BusinessException("不存在该任务，或者不允许拒绝") ;
        }

        LambdaUpdateChainWrapper<ChangeDetail> up = new LambdaUpdateChainWrapper<>(changeMapper) ;
        up.eq(ChangeDetail::getId,id) ;
        if ( pass ) {
            up.set(ChangeDetail::getChangeStatus, 1);
            //汇兑完成，目标货币入账
            BillDetail bill = new BillDetail() ;
            bill.setBillType(4) ;
            bill.setUserId(change.getUserId()) ;
            bill.setBillValue(change.getTargetValue()) ;
            bill.setCoinCode(change.getTargetCoin()) ;
            bill.setOuterId(change.getId()) ;

            recordDepBill(bill) ;
        } else {
            up.set(ChangeDetail::getChangeStatus, 3);
            up.set(ChangeDetail::getMemo,mime) ;
            //回写账本金额
            rollbackBal(change.getUserId(),change.getDepCoin(),3,change.getId(),4) ;
        }

        return up.update() ;
    }

    @Override
    public ChangeDetail getChangeDetail(Long id) {
        return changeMapper.selectOne(new QueryWrapper<ChangeDetail>().lambda()
                .eq(ChangeDetail::getId,id).eq(ChangeDetail::getUserId,getLoginUserId())) ;
    }

    @Override
    public void rollbackBal(Long userId, String coin, Integer billType, Long outId, Integer backType) {
        BalanceDetail bal = balanceMapper.selectOne(new QueryWrapper<BalanceDetail>().lambda()
                .eq(BalanceDetail::getUserId, userId)
                .eq(BalanceDetail::getCoinCode,coin)) ;

        BillDetail bill = billMapper.selectOne(new QueryWrapper<BillDetail>().lambda()
                .eq(BillDetail::getBillType,billType)
                .eq(BillDetail::getOuterId,outId)) ;
        if ( null == bal ) {
            throw new BusinessException("当前的货币账本未有入账，请联系客服处理") ;
        }
        if ( null == bill ) {
            throw new BusinessException("不存在换汇时的入账账单，请联系客服处理") ;
        }
        //汇兑划出了，取消后资金重新入账
        bill.setBillType(backType);
        bill.setBillBack(1) ;
        bill.setId(null);
        bill.setCreateTime(null);
        bill.setModifiedTime(null);

        LambdaUpdateChainWrapper<BalanceDetail> up = new LambdaUpdateChainWrapper<>(balanceMapper) ;
        up.eq(BalanceDetail::getUserId, bill.getUserId())
                .eq(BalanceDetail::getCoinCode,bill.getCoinCode()) ;
        up.setSql("balance = balance + "+ bill.getBillValue() +"") ;

        up.update() ;

        bal = balanceMapper.selectById(bal.getId()) ;
        bill.setBalance(bal.getBalance()) ;

        billMapper.insert(bill) ;
    }
}
