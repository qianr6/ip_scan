package com.gatning.ip_scan.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.gatning.ip_scan.entity.LocalIp;
import com.gatning.ip_scan.entity.ResultEntity;
import com.gatning.ip_scan.service.LocalIpService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Component
public class DDNS {

    @Value("${ali-config.accessId}")
    private String accessId;
    @Value("${ali-config.accessSecret}")
    private String accessSecret;

    @Value("${host.myFlag}")
    private String flag;

    @Value("${ali-config.dominAble}")
    private boolean dominAble;
    @Autowired
    private IpScanUtils ipScanUtils;

    @Autowired
    private LocalIpService localIpService;

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

    /**
     * 修改解析记录
     */
    private UpdateDomainRecordResponse updateDomainRecord(UpdateDomainRecordRequest request, IAcsClient client) {
        try {
            // 调用SDK发送请求
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            // 发生调用错误，抛出运行时异常
            throw new RuntimeException();
        }
    }

    private static void log_print(String functionName, Object result) {
        Gson gson = new Gson();
        System.out.println("-------------------------------" + functionName + "-------------------------------");
        System.out.println(gson.toJson(result));
    }

    public ResultEntity send() {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setFlag(flag);
        // 当前主机公网IP
        String currentHostIP = ipScanUtils.getCurrentHostIP();
        if (StringUtils.isEmpty(currentHostIP)) {
            resultEntity.setCode(300);
            resultEntity.setRemark("获取公网IP v6地址失败！请检查网络状态！");
        } else {
            //旧IP
            LocalIp oldiIps = localIpService.getByStatus(true,flag);
            if (oldiIps == null || !oldiIps.getIpAddr().equals(currentHostIP)) {
                if (dominAble) {
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
                    describeDomainRecordsRequest.setRRKeyWord("www");
                    // 解析记录类型
                    describeDomainRecordsRequest.setType("AAAA");
                    DescribeDomainRecordsResponse describeDomainRecordsResponse = describeDomainRecords(describeDomainRecordsRequest, client);
                    log_print("describeDomainRecords", describeDomainRecordsResponse);

                    List<DescribeDomainRecordsResponse.Record> domainRecords = describeDomainRecordsResponse.getDomainRecords();
                    // 最新的一条解析记录
                    if (domainRecords.size() != 0) {
                        DescribeDomainRecordsResponse.Record record = domainRecords.get(0);
                        // 记录ID
                        String recordId = record.getRecordId();
                        // 记录值
                        String recordsValue = record.getValue();
                        System.out.println("-------------------------------当前主机公网IP为：" + currentHostIP + "-------------------------------");
                        if (!currentHostIP.equals(recordsValue)) {
                            // 修改解析记录
                            UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest();
                            // 主机记录
                            updateDomainRecordRequest.setRR("www");
                            // 记录ID
                            updateDomainRecordRequest.setRecordId(recordId);
                            // 将主机记录值改为当前主机IP
                            updateDomainRecordRequest.setValue(currentHostIP);
                            // 解析记录类型
                            updateDomainRecordRequest.setType("AAAA");
                            UpdateDomainRecordResponse updateDomainRecordResponse = updateDomainRecord(updateDomainRecordRequest, client);
                            log_print("updateDomainRecord", updateDomainRecordResponse);

                            //向数据库添加新的IP地址记录

                            LocalIp localIp = new LocalIp();
                            localIp.setIpAddr(currentHostIP);
                            localIp.setCreatedDate(new Date());
                            localIp.setIpStatus(true);
                            localIp.setFlag(flag);
                            localIpService.insert(localIp);

                            //失效旧IP
                            if (null != oldiIps) {
                                oldiIps.setIpStatus(false);
                                localIpService.update(oldiIps);
                            }

                            resultEntity.setCode(200);
                            resultEntity.setRemark("更新成功!本次修改记录：" + currentHostIP);
                        } else {
                            resultEntity.setCode(100);
                            resultEntity.setRemark("当前IP地址与当前解析记录相同，不需要更新！");
                        }
                    } else {
                        resultEntity.setCode(300);
                        resultEntity.setRemark("获取阿里云解析记录失败，请检查域名配置！");
                    }
                } else {
                    //向数据库添加新的IP地址记录
                    LocalIp localIp = new LocalIp();
                    localIp.setIpAddr(currentHostIP);
                    localIp.setCreatedDate(new Date());
                    localIp.setIpStatus(true);
                    localIp.setFlag(flag);
                    localIpService.insert(localIp);
                    //失效旧IP
                    if (null != oldiIps) {
                        oldiIps.setIpStatus(false);
                        localIpService.update(oldiIps);
                    }
                    resultEntity.setCode(200);
                    resultEntity.setRemark("更新成功!本次修改记录：" + currentHostIP);
                }
            } else {
                resultEntity.setCode(100);
                resultEntity.setRemark("当前IP地址与当前解析记录相同，不需要更新！");
            }
        }

        return resultEntity;
    }
}
