package com.mmk.app;

import groovy.util.logging.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 项目启动类
 */
@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "com.mmk")
@EnableJpaRepositories(basePackages = {"com.mmk.etl.jpa.repository","com.mmk.rbac.jpa.repository"})
@EntityScan(basePackages = {"com.mmk.etl.jpa.entity","com.mmk.rbac.jpa.entity"})
@EnableScheduling
public class MindmarkApplication {
    public static void main(String[] args) {
        SpringApplication.run(MindmarkApplication.class, args);
    }
}
