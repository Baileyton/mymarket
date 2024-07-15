package com.stock;

import com.core.global.config.JpaAuditingConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Import(JpaAuditingConfig.class)
@EnableDiscoveryClient
@SpringBootApplication
public class StockApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class, args);
    }

    @PostConstruct
    public void setTime() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
