package com.rpay.service.sms;

/**
 * 信息服务，包括短信息和邮件
 * @author steven
 */
public interface MessageService {
    /**
     * 发送邮件
     * @param title 标题
     * @param code 验证码
     * @param time 时效性
     * @param mail 目标邮件地址
     */
    void sendMail(String title ,String code, String time, String mail) ;

    /**
     * 发送邮件
     * @param title 标题
     * @param content 邮件内容
     * @param mail 目标邮件地址
     */
    void sendMailForHtml(String title ,String content, String mail) ;

    /**
     * 发送短信验证码
     * @param code 验证码
     * @param time 时效
     * @param phone 目标电话
     * @param areaCode 区号
     */
    void sendSortMessage(String code, String time, String phone, String areaCode) ;
}
