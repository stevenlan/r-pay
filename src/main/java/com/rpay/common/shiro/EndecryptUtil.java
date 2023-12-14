package com.rpay.common.shiro;

import com.alibaba.fastjson.JSON;
import com.rpay.common.constant.CommonConstant;
import com.rpay.controller.vo.IndVO;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 加密解密工具类
 *
 * @author dinghao
 * @date 2021/3/12
 */
public class EndecryptUtil {

    /**
     * md5加密(没有盐)
     *
     * @param password 要加密的字符串
     */
    public static String encrytMd5(String password) {
        return new Md5Hash(password).toHex();
    }

    /**
     * 指定盐salt进行md5加密
     *
     * @param password       要加密的字符串
     * @param salt           盐
     * @param hashIterations 散列次数
     * @return
     */
    public static String encrytMd5(String password, String salt, int hashIterations) {
        return new Md5Hash(password, salt, hashIterations).toHex();
    }

    public static String encrytMd5(String password, int hashIterations) {
        return encrytMd5(password, CommonConstant.DEFAULT_SALT, hashIterations);
    }

    public static void main(String[] args) {
        System.out.println(encrytMd5("admin", CommonConstant.DEFAULT_SALT, CommonConstant.DEFAULT_HASH_COUNT));
        /*String a = "{\n" +
                "\t\"birth\": \"1981-09-12\",\n" +
                "\t\"email\": \"28195327@qq.com\",\n" +
                "\t\"phone\": \"+8618823387812\",\n" +
                "\t\"profile\": \"abogaosohgohog,gohouhg,hhsuhuhaushohg\",\n" +
                "\t\"somalia\": \"No\",\n" +
                "\t\"sources\": \"work\",\n" +
                "\t\"lastName\": \"Li\",\n" +
                "\t\"firstName\": \"steven\",\n" +
                "\t\"resAddress\": \"abcd, hhgbs road, 009864bg, iughsoughouhuog\",\n" +
                "\t\"citizenship\": \"china\",\n" +
                "\t\"publicFigure\": \"No\"\n" +
                "}" ;
        String b = "{\n" +
                "\t\"soa\": \"76685SWIFT\",\n" +
                "\t\"bankName\": \"cmbc\",\n" +
                "\t\"bankAddress\": \"oadhgohgh,hushugauo road, 876\",\n" +
                "\t\"bankCountry\": \"china\",\n" +
                "\t\"accountNumber\": \"9876512145\",\n" +
                "\t\"beneficiaryName\": \"Lan Steven\",\n" +
                "\t\"beneficiaryAddress\": \"ShenZhen\"\n" +
                "}" ;
        String c = "{\n" +
                "\t\"poaPath\": \"group1/M00/00/00/wKgfMmHo3ySAQ98TAAfiST2lCfQ413.pdf\",\n" +
                "\t\"sofPath\": \"group1/M00/00/00/wKgfMmHo30GADboAAADgORBiQQQ16.docx\",\n" +
                "\t\"passportPath\": \"group1/M00/00/00/wKgfMmHo3yCAMx6UABsdTue6P24131.pdf\",\n" +
                "\t\"signaturePath\": \"group1/M00/00/00/wKgfMmHo3xqAb0dGAACDLaaq9Zg313.jpg\"\n" +
                "}" ;
        IndVO ind1 = JSON.parseObject(a, IndVO.class) ;
        IndVO ind2 = JSON.parseObject(b, IndVO.class) ;
        IndVO ind3 = JSON.parseObject(c, IndVO.class) ;

        System.out.println(ind1);
        System.out.println(ind2);
        System.out.println(ind3);*/
    }
}
