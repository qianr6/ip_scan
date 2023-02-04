package com.gatning.ip_scan.task;

import com.gatning.ip_scan.entity.ResultEntity;
import com.gatning.ip_scan.entity.SimpleEmailEntity;
import com.gatning.ip_scan.utils.DDNS;
import com.gatning.ip_scan.utils.SendMailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IpScanTask {
    @Autowired
    private DDNS ddns;
    @Autowired
    private SendMailUtils sendMailUtils;


    /**
     * 扫描IP地址，每5分钟执行一次
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void ipAutoConfig() {
        System.out.println("IP地址扫描开始");
        ResultEntity send = ddns.send();
        log.info(send.getRemark());
        if(send.getCode() != 100) { //需要更新的状态，则需要发送邮件
            SimpleEmailEntity emailEntity = new SimpleEmailEntity();
            String[] tos = new String[] {"742632713@qq.com"};
            emailEntity.setSubject("IP地址扫描完成！");
            emailEntity.setContent("\n" + send.getRemark());
            emailEntity.setTos(tos);
            sendMailUtils.sendSimpleMail(emailEntity);
        }
    }

//    @Scheduled(cron = "0/30 * * * * ?")
//    public void bengbengbeng(){
//        log.info("=========此心跳信息表明进程仍然存活!=========");
//    }
}
