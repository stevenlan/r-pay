package com.rpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.common.exception.BusinessException;
import com.rpay.common.utils.SessionUtils;
import com.rpay.controller.vo.PerReqVO;
import com.rpay.mapper.CryptRequestMapper;
import com.rpay.mapper.ExchangeMapper;
import com.rpay.mapper.bill.BalanceDetailMapper;
import com.rpay.mapper.deposit.DepRequestMapper;
import com.rpay.mapper.withdraw.WithdrawRequestMapper;
import com.rpay.model.BankDetail;
import com.rpay.model.CryptRequest;
import com.rpay.model.Exchange;
import com.rpay.model.User;
import com.rpay.model.bill.BalanceDetail;
import com.rpay.model.bill.BillDetail;
import com.rpay.model.deposit.DepRequest;
import com.rpay.model.withdraw.WithdrawRequest;
import com.rpay.service.*;
import com.rpay.service.query.CryQuery;
import com.rpay.service.query.DepositQuery;
import com.rpay.service.query.ExQuery;
import com.rpay.service.query.WithdrawQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author steven
 */
@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService, SessionUtils {
    private final Logger log = LoggerFactory.getLogger(this.getClass()) ;

    private final DepRequestMapper depMapper ;
    private final WithdrawRequestMapper witMapper ;
    private final CryptRequestMapper cryMapper ;
    private final BalanceDetailMapper balMapper ;
    private final ExchangeMapper exMapper ;
    private final AccountService accountService ;
    private final BillService billService ;
    private final UserService userService ;

    @Override
    public DepRequest putDepReq(DepRequest dep) {
        if ( null != dep.getId() ) {
            DepRequest old = depMapper.selectById(dep.getId()) ;
            if ( old.getReqStatus() != 1 ) {
                throw new BusinessException("该入账单据已经不允许修改") ;
            }
            if (StringUtils.isBlank(dep.getReqProof()) ) {
                throw new BusinessException("确认汇款必须提交回款转正凭证和相关资料") ;
            }

            LambdaUpdateChainWrapper<DepRequest> depUp = new LambdaUpdateChainWrapper<>(depMapper) ;
            depUp.set(DepRequest::getReqProof,dep.getReqProof()) ;
            depUp.set(DepRequest::getReqStatus,2) ;
            depUp.set(StringUtils.isNotBlank(dep.getMemo()),DepRequest::getMemo,dep.getMemo()) ;
            depUp.eq(DepRequest::getId,dep.getId()) ;
            depUp.update() ;
            return dep ;
        }
        dep.setReqStatus(1) ;
        BankDetail bank = accountService.findBank(dep.getBankId()) ;
        if ( null == bank ) {
            throw new BusinessException("收款账户不存在") ;
        }
        BankDetail send = accountService.findBank(dep.getSendBank()) ;
        if ( null == send ) {
            throw new BusinessException("打款账户不存在") ;
        }
        dep.setAccountName(bank.getAccountName()) ;
        dep.setSendAccount(send.getAccountName()) ;
        dep.setUserId(getLoginUserId()) ;
        depMapper.insert(dep) ;
        return  dep ;
    }

    @Override
    public Page<DepRequest> queryDepList(DepositQuery<DepRequest> query) {
        QueryWrapper<DepRequest> que = new QueryWrapper<>() ;
        if (StringUtils.isNotBlank(query.getEmail()) ) {
            User user = userService.getOne(new QueryWrapper<User>().lambda().eq(User::getEmail, query.getEmail()));
            if (null == user) {
                return new Page<>();
            }
            que.lambda().eq(DepRequest::getUserId, user.getId()) ;
        }
        que.lambda().eq(StringUtils.isNotBlank(query.getCoinCode()), DepRequest::getCoinCode,query.getCoinCode())
                .eq(null != query.getStatus(),DepRequest::getReqStatus,query.getStatus())
                .gt(null != query.getStartTime(), DepRequest::getCreateTime, query.getStartTime())
                .lt(null != query.getEndTime(), DepRequest::getCreateTime, query.getEndTime())
                .orderByDesc(DepRequest::getModifiedTime) ;

        return depMapper.selectPage(query,que) ;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean perDeposit(PerReqVO dep) {
        DepRequest old = depMapper.selectById(dep.getId()) ;
        if ( null == old ) {
            throw new BusinessException("入账申请不存在.") ;
        }
        if ( old.getReqStatus() != 2 ) {
            throw new BusinessException("当前状态不允许审核.") ;
        }
        LambdaUpdateChainWrapper<DepRequest> depUp = new LambdaUpdateChainWrapper<>(depMapper) ;
        depUp.eq(DepRequest::getId,dep.getId()) ;
        depUp.set(StringUtils.isNotBlank(dep.getMemo()),DepRequest::getMemo,dep.getMemo()) ;
        Integer status ;
        if ( dep.getPass() ) {
            if (null == dep.getDepValue()) {
                throw new BusinessException("审核通过必须填写实际入账金额.") ;
            }
            status = 4 ;
        } else {
            status = 5 ;
        }
        depUp.set(DepRequest::getReqStatus,status) ;
        depUp.set(DepRequest::getDepValue, dep.getDepValue()) ;
        depUp.set(DepRequest::getRecorded,getLoginUserId()) ;

        if ( depUp.update() && 4 == status ) {
            //写入入账单
            BillDetail bill = new BillDetail() ;
            bill.setBillType(1) ;
            bill.setBillValue(dep.getDepValue()) ;
            bill.setCoinCode(old.getCoinCode()) ;
            bill.setRecorded(getLoginUserId()) ;
            bill.setOuterId(old.getId()) ;
            bill.setUserId(old.getUserId()) ;

            return billService.recordDepBill(bill) ;
        } else {
            return false;
        }
    }

    @Override
    public DepRequest findDepRequest(Long id) {
        return depMapper.selectById(id) ;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public WithdrawRequest putWithdrawReq(WithdrawRequest withdraw) {
        if ( StringUtils.isNotBlank(withdraw.getPayPass()) ) {
            //验证支付密码
            boolean flag = userService.checkPayPass(getLoginUserId(), withdraw.getPayPass()) ;
            if ( !flag ) {
                //输入错误计数
                throw new BusinessException("支付密码错误，请重新输入") ;
            }
        }
        /*
        不允许修改
        if ( null != withdraw.getId() ) {
            WithdrawRequest old = witMapper.selectById(withdraw.getId());
            if ( null == old ) {
                throw new BusinessException("传入的提款申请不存在") ;
            }
            if ( old.getReqStatus() != 1 ) {
                throw new BusinessException("提款单已经锁定，无法修改") ;
            }

            witMapper.updateById(withdraw) ;
            return withdraw ;
        }*/

        BankDetail bank = accountService.findBank(withdraw.getBankId()) ;
        if ( null == bank ) {
            throw new BusinessException("选择的提款接收账户不可用") ;
        }
        withdraw.setAccountName(bank.getAccountName()) ;
        withdraw.setReqStatus(1) ;
        withdraw.setUserId(getLoginUserId()) ;

        //withdraw.setChangeValue(comChange(change, withdraw.getReqValue())) ;

        witMapper.insert(withdraw) ;

        //写入出账单
        BillDetail bill = new BillDetail();
        bill.setBillType(2);
        bill.setBillValue(withdraw.getReqValue());
        bill.setCommission(withdraw.getCommission());
        bill.setCoinCode(withdraw.getCoinCode());
        bill.setRecorded(getLoginUserId());
        bill.setOuterId(withdraw.getId());
        bill.setUserId(withdraw.getUserId()) ;

        billService.recordDepBill(bill);
        return withdraw ;
    }

    @Override
    public Page<WithdrawRequest> queryWitList(WithdrawQuery<WithdrawRequest> query) {
        QueryWrapper<WithdrawRequest> que = new QueryWrapper<>() ;
        if (StringUtils.isNotBlank(query.getEmail()) ) {
            User user = userService.getOne(new QueryWrapper<User>().lambda().eq(User::getEmail, query.getEmail()));
            if (null == user) {
                return new Page<>();
            }
            que.lambda().eq(WithdrawRequest::getUserId, user.getId()) ;
        }
        que.lambda().eq(StringUtils.isNotBlank(query.getCoinCode()),WithdrawRequest::getCoinCode,query.getCoinCode())
                .eq( null != query.getStatus(), WithdrawRequest::getReqStatus, query.getStatus())
                .gt( null != query.getStartTime(), WithdrawRequest::getCreateTime, query.getStartTime())
                .lt( null!= query.getEndTime(), WithdrawRequest::getCreateTime, query.getEndTime() )
                .orderByDesc(WithdrawRequest::getModifiedTime) ;
        return witMapper.selectPage(query,que) ;
    }

    @Override
    public WithdrawRequest findWithdrawRequest(Long id) {
        return witMapper.selectById(id) ;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean perWithdrawRequest(PerReqVO wit) {
        WithdrawRequest old = findWithdrawRequest(wit.getId()) ;
        if ( null == old ) {
            throw new BusinessException("提款申请不存在") ;
        }
        if ( old.getReqStatus() >= 3 ) {
            throw new BusinessException("该提款申请不能重复审批") ;
        }
        LambdaUpdateChainWrapper<WithdrawRequest> up = new LambdaUpdateChainWrapper<>(witMapper) ;
        up.eq(WithdrawRequest::getId, wit.getId()) ;
        //如果不通过，则直接返回
        if ( !wit.getPass() ) {
            up.set(WithdrawRequest::getReqStatus,5) ;
            up.set(StringUtils.isNotBlank(wit.getMemo()), WithdrawRequest::getMemo, wit.getMemo()) ;

            //回滚账单
            billService.rollbackBal(old.getUserId(),old.getCoinCode(),2,old.getId(),1);
            return up.update() ;
        }
        //如果通过，则判断
        if ( old.getReqStatus() == 1 ) {
            up.set(WithdrawRequest::getReqStatus,2) ;
        } else {
            up.set(WithdrawRequest::getReqStatus,3) ;
            if ( null == wit.getWithdrawValue() ) {
                throw new BusinessException("必须填写实际提款金额") ;
            }
            if ( null == wit.getCommission() ) {
                throw new BusinessException("必须填写手续费") ;
            }
            up.set(WithdrawRequest::getWithdrawValue, wit.getWithdrawValue()) ;
            up.set(WithdrawRequest::getCommission, wit.getCommission()) ;
            up.set(StringUtils.isNotBlank(wit.getMemo()), WithdrawRequest::getMemo, wit.getMemo()) ;
            up.set(StringUtils.isNotBlank(wit.getProof()), WithdrawRequest::getWithdrawProof, wit.getProof()) ;
        }
        up.set(WithdrawRequest::getRecorded, getLoginUserId()) ;

        return up.update() ;
    }

    @Override
    public CryptRequest putCryptRequest(CryptRequest req) {
        //提款,计算汇率
        if ( req.getReqType() == 2 && StringUtils.isNotBlank(req.getPayPass()) ) {
            //验证支付密码
            boolean flag = userService.checkPayPass(getLoginUserId(), req.getPayPass()) ;
            if ( !flag ) {
                //输入错误计数
                throw new BusinessException("支付密码错误，请重新输入") ;
            }
        }
        req.setReqStatus(1) ;
        //if ( 2 == req.getReqType() ) {
//            if ( null == req.getSrcCode() ) {
//                throw new BusinessException("未选择要提取的账户货币") ;
//            }
//            ExQuery q = new ExQuery() ;
//            q.setExFrom(req.getSrcCode()) ;
//            q.setExTarget(req.getCoinCode()) ;
//            Exchange change = accountService.matchExchange(q) ;
//            if ( null == change ) {
//                throw new BusinessException("当前不支持该种货币兑换") ;
//            }
            //req.setWitValue(comChange(change, req.getReqValue())); ;
        //}
        req.setUserId(getLoginUserId()) ;
        if ( null != req.getId() && req.getReqType() == 1 ) {
            CryptRequest old = cryMapper.selectById(req.getId()) ;
            if ( old.getReqStatus() != 1 ) {
                throw new BusinessException("该申请单已锁定，不允许修改") ;
            }
            new LambdaUpdateChainWrapper<CryptRequest>(cryMapper).eq(CryptRequest::getId,req.getId())
                    .set(CryptRequest::getReqProof,req.getReqProof()).update() ;
        } else {
            cryMapper.insert(req);
        }

        if (req.getReqType() == 2) {
            BillDetail bill = new BillDetail();
            bill.setBillType(req.getReqType());
            bill.setUserId(req.getUserId());
//            if ( 1==req.getReqType() ) {
            bill.setBillValue(req.getReqValue());
            bill.setCoinCode(req.getCoinCode());
//            } else {
//                bill.setBillValue(req.getReqValue());
//                bill.setCoinCode(req.getSrcCode()) ;
//            }

            bill.setRecorded(getLoginUserId());
            bill.setOuterId(req.getId());

            billService.recordDepBill(bill);
        }
        return req ;
    }

    @Override
    public Page<CryptRequest> queryCryReq(CryQuery<CryptRequest> query) {
        QueryWrapper<CryptRequest> que = new QueryWrapper<>() ;
        if (StringUtils.isNotBlank(query.getEmail()) ) {
            User user = userService.getOne(new QueryWrapper<User>().lambda().eq(User::getEmail, query.getEmail()));
            if (null == user) {
                return new Page<>();
            }
            que.lambda().eq(CryptRequest::getUserId, user.getId()) ;
        }
        que.lambda().eq(CryptRequest::getReqType, query.getType())
                .eq(null != query.getStatus(), CryptRequest::getReqStatus, query.getStatus())
                .gt(null != query.getStartTime(), CryptRequest::getCreateTime, query.getStartTime())
                .lt(null != query.getEndTime(), CryptRequest::getCreateTime, query.getEndTime())
                .orderByDesc(CryptRequest::getModifiedTime) ;
        return cryMapper.selectPage(query, que) ;
    }

    @Override
    public CryptRequest findCryReq(Long id) {
        return cryMapper.selectById(id) ;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean perCryReq(PerReqVO per) {
        CryptRequest req = findCryReq(per.getId()) ;
        LambdaUpdateChainWrapper<CryptRequest> up = new LambdaUpdateChainWrapper<>(cryMapper) ;
        if ( null == req ) {
            throw new BusinessException("该审批申请不存在") ;
        }
        if ( 1 != req.getReqStatus() ) {
            throw new BusinessException("该申请已经不能审批") ;
        }
        up.eq(CryptRequest::getId, per.getId()) ;

        Integer status ;
        if ( per.getPass() ) {
            status = 2 ;
        } else {
            status = 3 ;
            up.set(StringUtils.isNotBlank(per.getMemo()), CryptRequest::getMemo, per.getMemo()) ;
        }
        up.set(CryptRequest::getReqStatus, status) ;
        up.set(StringUtils.isNotBlank(per.getProof()), CryptRequest::getReqProof, per.getProof()) ;
        up.set(CryptRequest::getRecorded, getLoginUserId()) ;
        up.set(req.getReqType() == 2 && StringUtils.isNotBlank(per.getTid())
                ,CryptRequest::getTid, per.getTid()) ;

        boolean flag = up.update() ;
        if ( flag ) {
            if ( req.getReqType() == 2 && !per.getPass() ) {
                billService.rollbackBal(req.getUserId(), req.getCoinCode(), 2, req.getId(), 1);
            }
            //入账在此时才记录账单
            if ( req.getReqType() == 1 && per.getPass() ) {
                BillDetail bill = new BillDetail();
                bill.setBillType(req.getReqType());
                bill.setUserId(req.getUserId());
//            if ( 1==req.getReqType() ) {
                bill.setBillValue(req.getReqValue());
                bill.setCoinCode(req.getCoinCode());
//            } else {
//                bill.setBillValue(req.getReqValue());
//                bill.setCoinCode(req.getSrcCode()) ;
//            }

                bill.setRecorded(getLoginUserId());
                bill.setOuterId(req.getId());

                billService.recordDepBill(bill);
            }
        }
        return flag ;
    }

    @Override
    public List<BalanceDetail> queryBalances(Long id) {
        QueryWrapper<BalanceDetail> query = new QueryWrapper<>() ;
        query.lambda().eq(BalanceDetail::getUserId, id).ge(BalanceDetail::getBalance, 0) ;

        return balMapper.selectList(query) ;
    }

    @Override
    public List<String> withdrawCoins(String code, Integer type) {
        //暂时不需要type过滤
        return exMapper.withdrawCoinsForType(code) ;
    }
}
