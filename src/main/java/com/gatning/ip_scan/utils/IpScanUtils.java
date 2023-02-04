package com.gatning.ip_scan.utils;

import org.springframework.stereotype.Component;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 扫描IP地址工具类
 */
@Component
public class IpScanUtils {

    /**
     * 获取IP地址
     * @return 第一条IPV6公网地址
     */
    public String getIpAddress() {
        try {
            //获取所有网卡
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                //过滤回环地址、虚拟网卡、未使用的网卡
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        //过滤IPv6地址
                        if (ip instanceof Inet6Address) {
                            //排除本地私有地址，仅过滤240e开头的公网ipv6地址
                            if (ip.getHostAddress().startsWith("240e") && ip.getHostAddress().length() > 30) {
                                System.out.println("扫描到的地址：" + ip.getHostAddress());
                                return ip.getHostAddress().split("%")[0];
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("IP地址获取失败" + e.getMessage());
        }
        return null;
    }

}
