package com.mmk.etl.conf;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.zhipuai.ZhiPuAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 目前已经引入并测试： openAiChatModel 和 zhiPuAiChatModel
 * @author 大漠穷秋
 */
@Configuration
public class AiEmbeddingConfig {


    @Primary
    @Bean(name = "custZhiPuAiEmbeddingModel")
    public EmbeddingModel zhiPuAiEmbeddingModel(@Qualifier("zhiPuAiEmbeddingModel") ZhiPuAiEmbeddingModel embeddingModel) {
        return embeddingModel;
    }

//
//    /**
//     * TODO: FIXME 这里似乎不能直接包装 openAiEmbeddingModel ，因为 application.yml 中没有配置能够兼容 OpenAi 的嵌入模型
//     * @param embeddingModel
//     * @return
//     */
//    @Bean(name = "custOpenAiEmbeddingModel")
//    @Primary
//    public EmbeddingModel openAiEmbeddingModel(@Qualifier("openAiEmbeddingModel") OpenAiEmbeddingModel embeddingModel) {
//        return embeddingModel;
//    }
//
//
//    /**
//     * @param embeddingModel
//     * @return
//     */
//    @Bean(name = "custOllamaAiEmbeddingModel")
//    @Primary
//    public EmbeddingModel custOllamaAiEmbeddingModel(@Qualifier("ollamaEmbeddingModel") OllamaEmbeddingModel embeddingModel) {
//        return embeddingModel;
//    }
}
