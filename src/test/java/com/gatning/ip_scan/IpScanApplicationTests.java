package com.gatning.ip_scan;

import com.gatning.ip_scan.entity.LocalIp;
import com.gatning.ip_scan.service.LocalIpService;
import com.gatning.ip_scan.utils.DDNS;
import com.gatning.ip_scan.utils.IpScanUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
//        LocalIp byStatus = localIpService.getByStatus(true);
//        System.out.println(null == byStatus ? "未查询到有效IP" : byStatus.getIpAddr());
    }
}
