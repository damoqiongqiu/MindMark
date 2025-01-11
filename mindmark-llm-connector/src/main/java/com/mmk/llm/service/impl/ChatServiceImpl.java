package com.mmk.llm.service.impl;

import com.mmk.llm.constant.ModelType;
import com.mmk.llm.service.ChatService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 大漠穷秋
 */
@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    @Qualifier("zhiPuAiChatClient")
    @Resource
    private ChatClient zhiPuAiChatClient;

    @Qualifier("openAiChatClient")
    @Resource
    private ChatClient openAiChatClient;

    @Qualifier("ollamaAiChatClient")
    @Resource
    private ChatClient ollamaAiChatClient;

    @Resource
    private VectorStore vectorStore;

    private ChatClient getChatClient(String modelType) {
        switch (ModelType.normalize(modelType)) {
            case ModelType.OPENAI:
                return openAiChatClient;
            case ModelType.ZHIPUAI:
                return zhiPuAiChatClient;
            case ModelType.OLLAMA:
                return ollamaAiChatClient;
            default:
                return zhiPuAiChatClient;
        }
    }

    @Override
    public String chat(String msg) {
        return chat(msg, ModelType.DEFAULT);
    }

    @Override
    public String chat(String msg, String modelType) {
        log.info("Processing chat request for message: {}", msg);
        return getChatClient(modelType)
                .prompt()
                .user(msg)
                .call()
                .content();
    }

    @Override
    public Flux<String> chatStream(String msg) {
        return chatStream(msg, ModelType.DEFAULT);
    }

    @Override
    public Flux<String> chatStream(String msg, String modelType) {
        log.info("Starting chat stream for message: {}", msg);
        return getChatClient(modelType)
                .prompt()
                .user(msg)
                .stream()
                .content();
    }

    public String embed(String msg, Set<String> fileIds) {
        return embed(msg, fileIds, ModelType.DEFAULT);
    }

    public String embed(String msg, Set<String> fileIds, String modelType) {
        log.debug("embedding... {}", msg);

        Set<String> finalFileIds = (fileIds == null) ? new HashSet<>() : fileIds;

        // 首先查询向量库
        // TODO: 引入工具库，更好地识别用户的 prompt
        // TODO: 引入工具库，进行多路召回，并对结果重新进行 Ranking
        String promptContent = vectorStore
                .similaritySearch(SearchRequest.builder()
                        .query(msg)
                        .topK(5)
                        .build())
                .stream()
                .filter(doc -> {
                    if (CollectionUtils.isEmpty(finalFileIds)) return true;
                    Object fileIdObject = doc.getMetadata().get("file_id");
                    String docFileId = fileIdObject != null ? fileIdObject.toString() : null;
                    return finalFileIds.contains(docFileId);
                })
                .map(Document::getText)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining(" "));

        // 确保 promptContent 不为空
        if (!StringUtils.hasText(promptContent)) {
            promptContent = "No relevant information found in the database.";
        }

        log.debug("Prompt content: {}", promptContent);

        return getChatClient(modelType)
                .prompt(promptContent)  //把 prompt 传递给大模型
                .user(msg)              //用户发送的内容
                .call()
                .content();
    }
}
