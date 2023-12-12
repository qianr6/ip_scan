package com.gatning.ip_scan.task;

import com.gatning.ip_scan.entity.ResultEntity;
import com.gatning.ip_scan.entity.SimpleEmailEntity;
import com.gatning.ip_scan.utils.DDNS;
import com.gatning.ip_scan.utils.HttpClientUtils;
import com.gatning.ip_scan.utils.SendMailUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class IpScanTask {
    @Autowired
    private DDNS ddns;
    @Autowired
    private SendMailUtils sendMailUtils;

    @Value("${ali-config.dingding.robotMsgUrl}")
    private String msgUrl;

    @Value("${ali-config.dingding.robotSecret}")
    private String secret;

    @Value("${ali-config.dingding.robotAccesTtoken}")
    private String accesToken;


    /**
     * 扫描IP地址，每5分钟执行一次
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void ipAutoConfig() {
        System.out.println("IP地址扫描开始");
        ResultEntity send = ddns.send();
        log.info(send.getRemark());
        if(send.getCode() != 100) {//需要更新的状态，则需要发送邮件
            try {
                sendMsg(send);
            } catch (Exception exception) {
                log.error("发送钉钉消息失败：" + exception.getMessage());
            }
//            SimpleEmailEntity emailEntity = new SimpleEmailEntity();
//            String[] tos = new String[] {"742632713@qq.com"};
//            emailEntity.setSubject("IP地址扫描完成！");
//            emailEntity.setContent("\n" + send.getRemark());
//            emailEntity.setTos(tos);
//            sendMailUtils.sendSimpleMail(emailEntity);
        }
    }

//    @Scheduled(cron = "0/30 * * * * ?")
//    public void bengbengbeng(){
//        log.info("=========此心跳信息表明进程仍然存活!=========");
//    }



    public String sendMsg(ResultEntity send) throws Exception{
        //根据secret获取sign
        //时间戳
        Long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        //最终sign
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
        //拼接ur
        String finalUrl = msgUrl + "?" + "access_token=" + accesToken + "&" + "sign=" + sign + "&" + "timestamp=" + timestamp;
        Map<String,Object> param = new HashMap<>();
        param.put("msgtype","text");
        Map<String,String> content = new HashMap<>();
        content.put("content", send.getRemark());
        param.put("text",content);
        //发送消息
        HttpClientUtils.post(finalUrl,param);
        return sign;
    }
}
