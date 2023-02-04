package com.gatning.ip_scan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.gatning.ip_scan.dao")
public class IpScanApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpScanApplication.class, args);
    }

}
