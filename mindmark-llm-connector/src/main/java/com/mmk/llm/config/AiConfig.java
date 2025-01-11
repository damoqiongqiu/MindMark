package com.mmk.llm.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 目前已经引入并测试： openAiChatModel 和 zhiPuAiChatModel
 * @author 大漠穷秋
 */
@Configuration
public class AiConfig {
    @Primary
    @Bean
    public ChatClient zhiPuAiChatClient(@Qualifier("zhiPuAiChatModel") ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

    @Primary
    @Bean(name = "custZhiPuAiEmbeddingModel")
    public EmbeddingModel zhiPuAiEmbeddingModel(@Qualifier("zhiPuAiEmbeddingModel") EmbeddingModel embeddingModel) {
        return embeddingModel;
    }

    @Bean
    public ChatClient openAiChatClient(@Qualifier("openAiChatModel") ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

    /**
     * TODO: FIXME 这里似乎不能直接包装 openAiEmbeddingModel ，因为 application.yml 中没有配置能够兼容 OpenAi 的嵌入模型
     * @param embeddingModel
     * @return
     */
    @Bean(name = "custOpenAiEmbeddingModel")
    public EmbeddingModel openAiEmbeddingModel(@Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel) {
        return embeddingModel;
    }

    @Bean(name = "ollamaAiChatClient")
    public ChatClient ollamaAiChatClient(@Qualifier("ollamaChatModel") ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

    /**
     * @param embeddingModel
     * @return
     */
    @Bean(name = "custOllamaAiEmbeddingModel")
    public EmbeddingModel custOllamaAiEmbeddingModel(@Qualifier("ollamaEmbeddingModel") OllamaEmbeddingModel embeddingModel) {
        return embeddingModel;
    }
}
