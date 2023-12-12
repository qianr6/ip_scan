package com.gatning.ip_scan.utils;

import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
/**
 * 扫描IP地址工具类
 */
@Component
public class IpScanUtils {

    @Value("${ip.getIpUrl}")
    private String getIpUrl;

    /**
     * 获取IP地址
     *
     * @return 第一条IPV6公网地址
     */
    public List<String> getIpAddress() {
        List<String> ipList = new ArrayList<>();
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
                                ipList.add(ip.getHostAddress().split("%")[0]);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("IP地址获取失败" + e.getMessage());
        }
        return ipList;
    }

    /**
     * 通过向外网发送请求，来获取本机IP地址
     * 获取当前主机公网IP
     */
    public String getCurrentHostIP() {
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 设置连接超时时间为 5 秒
        factory.setReadTimeout(10000); // 设置读取超时时间为 10 秒
        restTemplate.setRequestFactory(factory);
        // 发送get请求，并用String数据格式接收
        String result = restTemplate.getForObject(getIpUrl, String.class);

        // 获取结果转 json
        //System.out.println(result);
        //JSONObject jsonObject =  JSON.parseObject(result);
        // 转为json后，则可以根据json的键值取出value
        //String value = (String) jsonObject.get("ip");
        if (!StringUtils.isEmpty(result) && result.startsWith("240")) {
            return result;
        } else {
            return null;
        }

    }

}
