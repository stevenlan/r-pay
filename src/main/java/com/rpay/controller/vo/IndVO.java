package com.rpay.controller.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author steven
 */
@Data
public class IndVO {
    /** 基本信息 */
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String resAddress;
    private String citizenship;
    private String birth;
    private String profile;
    private String sources;
    private String publicFigure;
    private String somalia;

    public Map<String,String> baseInfo() {
        Map<String,String> map = new HashMap<>() ;
        map.put("email",email);
        map.put("firstName",firstName);
        map.put("lastName",lastName);
        map.put("phone",phone);
        map.put("resAddress",resAddress);
        map.put("citizenship",citizenship);
        map.put("birth",birth);
        map.put("profile",profile);
        map.put("sources",sources);
        map.put("publicFigure",publicFigure);
        map.put("somalia",somalia);
        return map ;
    }
    /** 银行信息 */
    private String bankName;
    private String bankAddress;
    private String bankCountry;
    private String beneficiaryName;
    private String accountNumber;
    private String beneficiaryAddress;
    private String soa;

    public Map<String,String> bankInfo() {
        Map<String,String> map = new HashMap<>() ;
        map.put("bankName",bankName);
        map.put("bankAddress",bankAddress);
        map.put("bankCountry",bankCountry);
        map.put("beneficiaryName",beneficiaryName);
        map.put("accountNumber",accountNumber);
        map.put("beneficiaryAddress",beneficiaryAddress);
        map.put("soa",soa);
        return map ;
    }
    /** 附件信息 */
    private String signaturePath ;
    private String passportPath ;
    private String poaPath ;
    private String sofPath ;
    public Map<String,String> attInfo() {
        Map<String,String> map = new HashMap<>() ;
        map.put("signaturePath",signaturePath);
        map.put("passportPath",passportPath);
        map.put("poaPath",poaPath);
        map.put("sofPath",sofPath);
        return map ;
    }
}
