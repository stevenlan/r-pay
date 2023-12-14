package com.rpay.controller.vo;

import lombok.Data;

/**
 * @author steven
 */
@Data
public class CoinVO {
    /**
     * 货币符号
     */
    private String coin ;
    /**
     * 余额
     */
    private String value ;
}
