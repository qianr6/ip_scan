package com.gatning.ip_scan.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * ip记录表(LocalIp)实体类
 *
 * @author makejava
 * @since 2022-10-07 20:17:12
 */
@Data
public class LocalIp implements Serializable {
    private static final long serialVersionUID = -36287644081289555L;
    /**
     * id
     */
    private Integer id;
    /**
     * ip地址
     */
    private String ipAddr;
    /**
     * 创建时间
     */
    private Date createdDate;
    /**
     * 状态 0:有效 1：失效
     */
    private Boolean ipStatus;

    /**
     * 主机身份标识
     */
    private String flag;




}

