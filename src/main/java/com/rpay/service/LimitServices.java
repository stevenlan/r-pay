package com.rpay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.model.CoinLimit;

/**
 * 限额接口，都放在这
 * @author steven
 */
public interface LimitServices {
    /**
     * 无条件的限额分页查询
     * @return
     */
    Page<CoinLimit> limitPage(Page<CoinLimit> query) ;

    /**
     * 货币限额的查询
     * @param coin
     * @param type
     * @return
     */
    CoinLimit limitGet(String coin, String type) ;

    /**
     * 计算限额
     * @param coin 货币
     * @param act 操作
     * @param value 价值
     * @return
     */
    void calculateLimit(String coin, String act, Double value) ;
}
