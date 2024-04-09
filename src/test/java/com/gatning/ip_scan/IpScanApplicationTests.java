package com.gatning.ip_scan;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.gatning.ip_scan.service.LocalIpService;
import com.gatning.ip_scan.utils.DDNS;
import com.gatning.ip_scan.utils.IpScanUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@SpringBootTest
class IpScanApplicationTests {


    @Autowired
    private IpScanUtils ipScanUtils;

    @Value("${ali-config.accessId}")
    private String accessId;
    @Value("${ali-config.accessSecret}")
    private String accessSecret;



    @Test
    public void getIpAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip instanceof Inet6Address) {
                            if (ip.getHostAddress().startsWith("240e")) {
                                System.out.println(ip.getHostAddress());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("IP地址获取失败" + e.getMessage());
        }
    }

    @Test
    public void testIpConfig() {
        String currentHostIP = ipScanUtils.getCurrentHostIP();
    }

    @Test
    public void testMoreDomin() {
        // 设置鉴权参数，初始化客户端
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-chengdu",// 地域ID
                accessId,// 您的AccessKey ID
                accessSecret);// 您的AccessKey Secret
        IAcsClient client = new DefaultAcsClient(profile);
        // 查询指定二级域名的最新解析记录
        DescribeDomainRecordsRequest describeDomainRecordsRequest = new DescribeDomainRecordsRequest();
        // 主域名
        describeDomainRecordsRequest.setDomainName("gatning.top");
        // 主机记录
        //describeDomainRecordsRequest.setRRKeyWord("note");
        // 解析记录类型
        describeDomainRecordsRequest.setType("A");
        DescribeDomainRecordsResponse describeDomainRecordsResponse = describeDomainRecords(describeDomainRecordsRequest, client);
        System.out.printf("describeDomainRecords", describeDomainRecordsResponse);

        List<DescribeDomainRecordsResponse.Record> domainRecords = describeDomainRecordsResponse.getDomainRecords();
        // 最新的一条解析记录
        if (domainRecords.size() != 0) {
            DescribeDomainRecordsResponse.Record record = domainRecords.get(0);
            // 记录ID
            String recordId = record.getRecordId();
            // 记录值
            String recordsValue = record.getValue();
            //System.out.println("-------------------------------当前主机公网IP为：" + currentHostIP + "-------------------------------");
            //if (!currentHostIP.equals(recordsValue)) {
                // 修改解析记录
//                UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest();
//                // 主机记录
//                updateDomainRecordRequest.setRR("www");
//                // 记录ID
//                updateDomainRecordRequest.setRecordId(recordId);
//                // 将主机记录值改为当前主机IP
//                updateDomainRecordRequest.setValue(currentHostIP);
//                // 解析记录类型
//                updateDomainRecordRequest.setType("AAAA");
//                UpdateDomainRecordResponse updateDomainRecordResponse = updateDomainRecord(updateDomainRecordRequest, client);
//                log_print("updateDomainRecord", updateDomainRecordResponse);
//
//                //向数据库添加新的IP地址记录
//
//                LocalIp localIp = new LocalIp();
//                localIp.setIpAddr(currentHostIP);
//                localIp.setCreatedDate(new Date());
//                localIp.setIpStatus(true);
//                localIp.setFlag(flag);
//                localIpService.insert(localIp);
//
//                //失效旧IP
//                if (null != oldiIps) {
//                    oldiIps.setIpStatus(false);
//                    localIpService.update(oldiIps);
//                }
//
//                resultEntity.setCode(200);
//                resultEntity.setRemark("更新成功!本次修改记录：" + currentHostIP);
//            } else {
//                resultEntity.setCode(100);
//                resultEntity.setRemark("当前IP地址与当前解析记录相同，不需要更新！");
//            }
        }
    }

    /**
     * 获取主域名的所有解析记录列表
     */
    private DescribeDomainRecordsResponse describeDomainRecords(DescribeDomainRecordsRequest request, IAcsClient client) {
        try {
            // 调用SDK发送请求
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            // 发生调用错误，抛出运行时异常
            throw new RuntimeException();
        }
    }

}
