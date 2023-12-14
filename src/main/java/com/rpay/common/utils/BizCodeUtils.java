package com.rpay.common.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author steven
 */
public class BizCodeUtils {
    public static String romUniqueId() {
        return romUniqueId(8) ;
    }

    public static String romUniqueId(int length) {
        return RandomStringUtils.random(length,true,true) ;
    }
    /**
     * 生成12位随机数
     *
     * @return
     */
    public static String getTXMcode() {
        double v = ((Math.random() * 9 + 1) * 100000000000.0);
        Long v2 = (long) v;
        return v2.toString();
    }

    public static void main(String[] args) {
        System.out.println(romUniqueId());
        System.out.println(StringUtils.substringAfter("666@123.com","@"));
        System.out.println(StringUtils.substringBefore("666@123.com","@"));
        System.out.println(StringUtils.substring("666",0,2));
    }
}
