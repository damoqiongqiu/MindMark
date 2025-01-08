package com.mmk.llm.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AiConfig {
    //可选项有： openAiChatModel 和 zhiPuAiChatModel
    @Primary
    @Bean
    public ChatClient chatClient(@Qualifier("zhiPuAiChatModel") ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

    @Primary
    @Bean
    public EmbeddingModel primaryEmbeddingModel(@Qualifier("zhiPuAiEmbeddingModel") EmbeddingModel zhiPuAiEmbeddingModel) {
        return zhiPuAiEmbeddingModel;
    }
}
