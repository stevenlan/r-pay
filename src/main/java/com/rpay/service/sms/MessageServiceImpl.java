package com.rpay.service.sms;

import cn.hutool.extra.mail.MailUtil;
import com.apistd.uni.Uni;
import com.apistd.uni.UniException;
import com.apistd.uni.UniResponse;
import com.apistd.uni.sms.UniMessage;
import com.apistd.uni.sms.UniSMS;
import com.rpay.common.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author steven
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{
    private String signString = "VerificationCod";
    @PostConstruct
    public void smsInit() {
        //Uni.init(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        //signString
    }
    @Override
    public void sendMail(String title, String code, String time, String mail) {
        String content = FileUtil.readContent("config/templates/checkCode.ftl") ;
        content = MessageFormat.format(content, code,time) ;
        sendMailForHtml(title,content,mail) ;
    }

    @Override
    public void sendPassMail(String title, String mail) {
        String content = FileUtil.readContent("config/templates/pass.ftl") ;
        sendMailForHtml(title,content,mail) ;
    }

    @Override
    public void sendMailForHtml(String title, String content, String mail) {
        MailUtil.send(mail,title,content, true) ;
    }

    @Override
    public void sendSortMessage(String code, String time, String phone, String areaCode) {
        // 设置自定义参数 (变量短信)
        Map<String, String> templateData = new HashMap<String, String>();
        templateData.put("code", code);
        templateData.put("ttl", time);

        if (!StringUtils.equals(areaCode,"+86")) {
            phone = areaCode + phone ;
        }

        // 构建信息
        UniMessage message = UniSMS.buildMessage()
                .setTo(phone)
                .setSignature(signString)
                .setTemplateId("pub_verif_register_ttl")
                .setTemplateData(templateData);

        // 发送短信
        try {
            UniResponse res = message.send();
            //计数
        } catch (UniException e) {
            log.error("sms "+e.requestId+" send error!",e);
            //System.out.println("Error: " + e);
            //System.out.println("RequestId: " + e.requestId);
        }
    }

    /**public static void main(String[] args) {
        Uni.init("m8B6MDzorPnKzasYGwjT2pVd8K8JnDUGsGeoTCeA7Y6mhyUNh");
        //VerificationCod
        Map<String, String> templateData = new HashMap<String, String>();
        templateData.put("code", "hg741");
        templateData.put("ttl", "15");

        UniMessage message = UniSMS.buildMessage()
                .setTo("+85244049187")
                .setSignature("VerificationCod")
                .setTemplateId("pub_verif_register_ttl")
                .setTemplateData(templateData);
        try {
            UniResponse res = message.send();
            //计数
            System.out.println(res);
        } catch (UniException e) {
            //log.error("sms "+e.requestId+" send error!",e);
            System.out.println("Error: " + e);
            System.out.println("RequestId: " + e.requestId);
        }
    }*/
}
