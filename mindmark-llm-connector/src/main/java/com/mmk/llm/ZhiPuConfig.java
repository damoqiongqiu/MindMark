package com.mmk.llm;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 大漠穷秋
 * 获取 yml 配置文件中智谱大模型相关的参数
 */
@Component
@ConfigurationProperties(prefix = "spring.ai.zhipuai.chat.options")
public class ZhiPuConfig {

    private double temperature;
    private String model;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
