package com.mmk.etl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * TODO:定时任务似乎应该作为一个独立的模块启动
 * @author 大漠穷秋
 */
@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "com.mmk")
@EnableScheduling
public class EtlApplication {

    public static void main(String[] args) {
        SpringApplication.run(EtlApplication.class, args);
    }

}
