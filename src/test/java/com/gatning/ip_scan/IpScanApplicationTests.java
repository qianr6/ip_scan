package com.gatning.ip_scan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gatning.ip_scan.entity.LocalIp;
import com.gatning.ip_scan.service.LocalIpService;
import com.gatning.ip_scan.utils.DDNS;
import com.gatning.ip_scan.utils.IpScanUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

@SpringBootTest
class IpScanApplicationTests {


    @Autowired
    private IpScanUtils ipScanUtils;

    @Autowired
    private DDNS ddns;

    @Autowired
    private LocalIpService localIpService;


//    @Test
//    void contextLoads() {
//        String ipAddress = ipScanUtils.getIpAddress();
//        System.out.println(ipAddress);
//    }


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
        RestTemplate restTemplate=new RestTemplate();
            // url 自行填写
            String url = "https://v6.myip.la/json";

            // 发送get请求，并用String数据格式接收
            String result = restTemplate.getForObject(url, String.class);

            // 获取结果转 json
            System.out.println(result);
            JSONObject jsonObject =  JSON.parseObject(result);

            // 转为json后，则可以根据json的键值取出value，
            // jsonObject..get()中填写键值（key）
            String value = (String) jsonObject.get("ip");
            System.out.println(value);
        }

}
