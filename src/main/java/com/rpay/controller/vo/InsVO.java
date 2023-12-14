package com.rpay.controller.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author steven
 */
@Data
public class InsVO {
    /** 基本信息 */
    private Long userId;
    private String companyName;
    private String country;
    private String regDate;
    private String busAddress;
    private String busCountry;
    private String webSite;
    private String busDetail;
    private String fundSource;
    private String federal;
    private String somalia;
    private String fullName;
    private String jobTitle;
    private String resEmail;
    private String phone;

    public Map<String,String> baseInfo() {
        Map<String,String> map = new HashMap<>() ;
        map.put("companyName",companyName);
        map.put("country",country);
        map.put("regDate",regDate);
        map.put("busAddress",busAddress);
        map.put("busCountry",busCountry);
        map.put("webSite",webSite);
        map.put("busDetail",busDetail);
        map.put("fundSource",fundSource);
        map.put("federal",federal);
        map.put("somalia",somalia);
        map.put("fullName",fullName);
        map.put("jobTitle",jobTitle);
        map.put("resEmail",resEmail);
        map.put("phone",phone);
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
    private String signaturePath;
    private String coiPath;
    private String associationPath;
    private String directorPath;
    private String rosPath;
    private String orgStrPath;
    private String comPoaPath;
    private String comSofPath;
    private String passportPath;
    private String poaPath;
    public Map<String,String> attInfo() {
        Map<String,String> map = new HashMap<>() ;
        map.put("signaturePath",signaturePath);
        map.put("coiPath",coiPath);
        map.put("associationPath",associationPath);
        map.put("directorPath",directorPath);
        map.put("rosPath",rosPath);
        map.put("orgStrPath",orgStrPath);
        map.put("comPoaPath",comPoaPath);
        map.put("comSofPath",comSofPath);
        map.put("passportPath",passportPath);
        map.put("poaPath",poaPath);
        return map ;
    }
}
