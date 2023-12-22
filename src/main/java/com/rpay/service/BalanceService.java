package com.rpay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.controller.vo.PerReqVO;
import com.rpay.model.CryptRequest;
import com.rpay.model.bill.BalanceDetail;
import com.rpay.model.deposit.DepRequest;
import com.rpay.model.withdraw.WithdrawRequest;
import com.rpay.service.query.CryQuery;
import com.rpay.service.query.DepositQuery;
import com.rpay.service.query.WithdrawQuery;

import java.util.List;

/**
 * 账单、出入金相关接口
 * @author steven
 */
public interface BalanceService {
    /**
     * 提交入金申请
     * @param dep 入金申请数据
     * @return
     */
    DepRequest putDepReq(DepRequest dep) ;

    /**
     * 查询充值入账申请列表
     * @param query
     * @return
     */
    Page<DepRequest> queryDepList(DepositQuery<DepRequest> query) ;

    /**
     * 获取单个入账申请详情
     * @param id 申请单id
     * @return
     */
    DepRequest findDepRequest(Long id) ;

    /**
     * 审核入账申请
     * @param dep 审核的对象
     * @return
     */
    boolean perDeposit(PerReqVO dep) ;

    /**
     * 提交出金申请
     * @param withdraw 出金申请数据
     * @return
     */
    WithdrawRequest putWithdrawReq(WithdrawRequest withdraw) ;

    /**
     * 查询提款单列表，带分页
     * @param query
     * @return
     */
    Page<WithdrawRequest> queryWitList(WithdrawQuery<WithdrawRequest> query) ;

    /**
     * 查找单个提款单详情
     * @param id 退款单id
     * @return
     */
    WithdrawRequest findWithdrawRequest(Long id) ;

    /**
     * 审批提款单
     * @param wit
     * @return
     */
    boolean perWithdrawRequest(PerReqVO wit) ;

    /**
     * 取消提现
     * @param reqId
     * @return
     */
    boolean cancelWithdraw(Long reqId) ;

    /**
     * 提交加密货币相关请求
     * @param req 请求信息
     * @return
     */
    CryptRequest putCryptRequest(CryptRequest req) ;

    /**
     * 查询加密货币申请列表
     * @param query 入参
     * @return
     */
    Page<CryptRequest> queryCryReq(CryQuery<CryptRequest> query) ;

    /**
     * 查看详情
     * @param id id
     * @return
     */
    CryptRequest findCryReq(Long id) ;

    /**
     * 审批加密货币申请
     * @param per 审批结果
     * @return
     */
    boolean perCryReq(PerReqVO per) ;

    /**
     * 取消申请
     * @param reqId
     * @return
     */
    boolean cancelReq(Long reqId) ;

    /**
     * 查询用户的账本列表
     * @param id 用户id
     * @return
     */
    List<BalanceDetail> queryBalances(Long id) ;

    /**
     * 平台支持兑换的货币
     * @param code 兑换的代码
     * @return
     */
    List<String> withdrawCoins(String code, Integer type) ;
}
