package com.rpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.common.exception.BusinessException;
import com.rpay.mapper.CoinLimitMapper;
import com.rpay.model.CoinLimit;
import com.rpay.service.LimitServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author steven
 */
@Service
@RequiredArgsConstructor
public class LimitServicesImpl implements LimitServices {
    private final CoinLimitMapper limitMapper ;

    @Override
    public Page<CoinLimit> limitPage(Page<CoinLimit> query) {
        return limitMapper.selectPage(query, new QueryWrapper<>()) ;
    }

    @Override
    public CoinLimit limitGet(String coin, String type) {
        QueryWrapper<CoinLimit> query = new QueryWrapper<>() ;
        query.lambda().eq(CoinLimit::getCoinCode, coin).eq(CoinLimit::getOptType,type) ;
        return limitMapper.selectOne(query) ;
    }

    @Override
    public void calculateLimit(String coin, String act, Double value) {
        CoinLimit limit  = limitGet(coin, act) ;
        /*if ( null == limit ) {
            throw new BusinessException("该货币未设置限额，暂时不能操作") ;
        }*/
        if ( null != limit ) {
            if ( value < limit.getCoinMin() ) {
                throw new BusinessException("不能小于单笔最小限额，请修改后再提交");
            }
            if ( null != limit.getCoinMax() && value > limit.getCoinMax() ) {
                throw new BusinessException("不能大于单笔最大限额，请修改后再提交");
            }
        } else {
            throw new BusinessException("该货币尚未开放操作，请联系客户经理");
        }
    }
}
