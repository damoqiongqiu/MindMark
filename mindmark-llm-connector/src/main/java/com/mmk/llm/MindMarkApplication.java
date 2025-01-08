package com.mmk.llm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 大漠穷秋
 */
@Slf4j
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
@ComponentScan(basePackages = "com.mmk")
@EnableScheduling
public class MindMarkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MindMarkApplication.class, args);
    }

}
