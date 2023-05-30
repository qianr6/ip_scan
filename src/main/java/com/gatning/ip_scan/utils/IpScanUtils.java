package com.gatning.ip_scan.utils;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 扫描IP地址工具类
 */
@Component
public class IpScanUtils {

    /**
     * 获取IP地址
     * @return 第一条IPV6公网地址
     */
    public List<String> getIpAddress() {
        List<String> ipList  = new ArrayList<>();
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

    /** 通过向外网发送请求，来获取本机IP地址
     * 获取当前主机公网IP
     */
    public String getCurrentHostIP(){
        // 这里使用jsonip.com第三方接口获取本地IP
        String jsonip = "https://ipv6.jsonip.com/";
        // 接口返回结果
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            // 使用HttpURLConnection网络请求第三方接口
            URL url = new URL(jsonip);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        return jsonObject.get("ip").toString();
    }

}
