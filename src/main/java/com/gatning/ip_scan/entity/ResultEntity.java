package com.gatning.ip_scan.entity;

import lombok.Data;

@Data
public class ResultEntity {
    /**
     * 代码: 100 地址已经存在;300 没有获取到解析记录;  200 成功
     */
    private Integer code;
    /**
     * 描述
     */
    private String remark;

    /**
     * 主机标志
     */
    private String flag;
}
