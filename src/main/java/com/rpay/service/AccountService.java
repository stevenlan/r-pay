package com.rpay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.model.BankDetail;
import com.rpay.model.Countries;
import com.rpay.model.Exchange;
import com.rpay.model.KycCertification;
import com.rpay.service.query.BankQuery;
import com.rpay.service.query.ExQuery;
import com.rpay.service.query.KycQuery;

import java.util.List;

/**
 * 账号、认证相关接口
 * @author steven
 */
public interface AccountService {
    /**
     * 添加或更新账户
     * @param bank 银行信息
     */
    boolean updateAccount(BankDetail bank) ;

    /**
     * 添加或更新认证信息
     * @param kyc 认证信息
     */
    boolean updateKyc(KycCertification kyc) ;

    /**
     * 添加或更新汇率信息
     * @param ex
     */
    boolean updateEx(Exchange ex) ;

    /**
     * 查询银行列表
     * @param userId 查询条件
     * @param query 查询条件
     * @return
     */
    List<BankDetail> findBanksByUser(Long userId , BankQuery query) ;

    /**
     * 分页查询
     * @param query 查询条件
     * @return
     */
    Page<BankDetail> pageBanksByUser(BankQuery query) ;

    /**
     * 获取银行数量
     * @param userId 所属用户
     * @param pass 是否通过审批
     * @return
     */
    int bankCount(Long userId, boolean pass) ;

    /**
     * 审批通过银行账户
     * @param bankId 银行ID
     * @param pass 是否通过
     * @param reason 拒绝原因说明
     * @return
     */
    boolean passBank(Long bankId, boolean pass, String reason) ;

    /**
     * 管理后台查询银行列表
     * @param query 查询条件
     * @return
     */
    List<BankDetail> findBanks(BankQuery query) ;

    /**
     * 查询指定银行账户信息
     * @param bankId 查询条件
     * @return
     */
    BankDetail findBank(Long bankId) ;

    /**
     * 管理后台查询认证信息
     * @param query 查询条件
     * @return
     */
    List<KycCertification> findKycList(KycQuery query) ;

    /**
     * 分页查询kyc
     * @param query 查询条件
     * @return
     */
    Page<KycCertification> pageKycList(KycQuery query) ;

    /**
     * 查询指定Kyc详情
     * @param userId 用户Id
     * @return
     */
    KycCertification findKyc(Long userId) ;

    /**
     * 删除银行
     * @param bankId 银行id
     * @param userId 用户id
     * @return
     */
    boolean bankDel(BankDetail bank, Long userId) ;

    /**
     * 查询指定Kyc详情
     * @param kycId kycId
     * @return
     */
    KycCertification findKycForId(Long kycId) ;

    /**
     * 审核通过Kyc
     * @param kycId kycID
     * @param pass 是否通过
     * @param reason 拒绝原因说明
     * @return
     */
    boolean passKyc(Long kycId, boolean pass, String reason) ;

    /**
     * 获取地区可选项列表
     * @return
     */
    List<Countries> getCountries() ;

    /**
     * 获取全部货币列表
     * @return
     */
    List<Countries> getCoins() ;

    /**
     * 获取加密可选列表
     * @return
     */
    List<Countries> getCrypts() ;

    /**
     * 根据条件获取地区
     * @param code
     * @return
     */
    Countries getCountry(String code) ;

    /**
     * 根据条件查询汇率
     * @param query 查询条件
     * @return
     */
    List<Exchange> exchanges(ExQuery query) ;

    /**
     * 匹配汇率
     * @param query 匹配汇率
     * @return
     */
    Exchange matchExchange(ExQuery query) ;
}
