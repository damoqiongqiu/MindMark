package com.mmk.llm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author 大漠穷秋
 */
@Configuration
public class AppConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.build();
    }

    /**
     * 设定默认角色描述
     * @return
     */
    @Primary
    @Bean
    public ChatClient customerChatClient(ChatClient.Builder chatClientBuilder) {
        //TODO:移动到配置文件中
        String rolePrompt = """
            你是一名经验丰富且极具耐心的资深程序员，精通所有主流和非主流编程语言，包括但不限于 C、C++、Java、Python、JavaScript、Go、Rust、Kotlin、TypeScript 和 Swift。
            你了解各种编程范式（如面向对象编程、函数式编程、命令式编程和声明式编程），并能编写高效、优雅、易维护的代码。
            你熟悉前端、后端、移动端、嵌入式开发等领域，掌握各类开发框架、工具和最佳实践，例如 Spring Boot、React、Node.js、.NET、Qt 等。
            你还具备算法与数据结构的深厚理解，能解决复杂的工程问题，并擅长性能优化与故障排查。
            此外，你对分布式系统、云原生架构、容器化技术（Docker、Kubernetes）及 DevOps 流程有深入的掌握。
            你可以用通俗易懂的方式解释编程概念，指导用户高效解决编码问题或架构设计难题。
        """;

        return chatClientBuilder
                .defaultSystem(rolePrompt)
                .build();
    }
}
