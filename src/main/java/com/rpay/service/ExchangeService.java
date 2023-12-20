package com.rpay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.model.*;
import com.rpay.service.query.CryAccQuery;

import java.util.List;
import java.util.Set;

/**
 * 汇率，收款银行账户及加密钱包地址设置
 * @author steven
 */
public interface ExchangeService {
    /**
     * 新增或者编辑汇率数据
     * @param exchange
     * @return
     */
    boolean updateExchange(Exchange exchange) ;

    /**
     * 获取汇率列表
     * @return
     */
    List<Exchange> exchanges() ;

    /**
     * 获取汇率分页列表
     * @param query
     * @return
     */
    Page<Exchange> exchangesPage(Page<Exchange> query) ;

    /**
     * 删除汇率
     * @param id
     * @return
     */
    boolean delExchange(Long id) ;

    /**
     * 新增或者更新收款账户设置
     * @param dep
     * @return
     */
    boolean updateDeposit(BankDetail dep) ;

    /**
     * 删除银行和货币的绑定关系
     * @param bankId
     */
    void delBindCoins(Long bankId) ;

    /**
     * 绑定银行和货币关联
     * @param coins
     * @param bankId
     */
    void bindCoins(List<String> coins, Long bankId) ;

    /**
     * 获取收款银行账户列表
     * @return
     */
    List<Deposit> deposits() ;

    /**
     * 获取绑定货币的银行id
     * @param coin
     * @return
     */
    List<Deposit> coinBankIds(String coin) ;

    /**
     * 获取银行全部的绑定货币
     * @param bankId
     * @return
     */
    List<Deposit> bankCoins(Long bankId) ;

    /**
     * 获取分页收款账户
     * @param query 分页条件
     * @return
     */
    Page<Deposit> depositPage(Page<Deposit> query) ;

    /**
     * 删除收款银行账户
     * @param id
     * @return
     */
    boolean delDeposit(Long id) ;

    /**
     * 设置加密钱包
     * @param cryAcc
     * @return
     */
    boolean updateCry(CryAccount cryAcc) ;

    /**
     * 用户设置提款地址
     * @param cryAcc
     * @return
     */
    boolean updateOutCry(CryAccount cryAcc) ;

    /**
     * 获得加密货币钱包地址
     * @param cryCode 加密货币代码
     * @param userId 所属用户
     * @return
     */
    List<CryAccount> findCry(String cryCode, Long userId) ;

    /**
     * 分页查询用户提款地址
     * @param query
     * @return
     */
    Page<CryAccount> pageCryAcc(CryAccQuery query) ;

    /**
     * 获取用户提款地址列表
     * @return
     */
    List<CryAccount> listCryAcc(String agreement, String cryCode) ;

    /**
     * 全部的来源货币
     * @return
     */
    List<Countries> queryExSource() ;

    /**
     * 查询目标货币
     * @param fromCountry 来源货币国家code
     * @return
     */
    List<Countries> queryExTarget(String fromCountry) ;

    /**
     * 获取所有支持使用的货币
     * @return
     */
    Set<String> queryAllExCoin() ;
}
