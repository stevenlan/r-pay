package com.rpay.common.utils;

/**
 * @author steven
 */

public enum LimitEnum {
    //limit的影响类型
    Deposit("dp"), Withdraw("wd"), Change("c") ;
    private String actType ;

    LimitEnum(String _actType) {
        this.actType = _actType ;
    }

    public String getActType() {
        return this.actType ;
    }
}
