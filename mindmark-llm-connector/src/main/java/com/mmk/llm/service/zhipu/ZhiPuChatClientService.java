package com.mmk.llm.service.zhipu;

import com.mmk.llm.service.ChatClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
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
@AllArgsConstructor
public class ZhiPuChatClientService implements ChatClientService {

    private final ChatClient chatClient;

    /**
     * TODO: 支持同时使用多种向量数据库。
     */
    private final VectorStore vectorStore;

    @Override
    public String chat(String msg) {
        log.info("Processing chat request for message: {}", msg);
        return chatClient
                .prompt()
                .user(msg)
                .call()
                .content();
    }

    @Override
    public Flux<String> chatStream(String msg) {
        log.info("Starting chat stream for message: {}", msg);
        return chatClient
                .prompt()
                .user(msg)
                .stream()
                .content();
    }

    /**
     * 根据用户消息和指定的文件ID进行查询
     * @param msg
     * @param fileIds
     * @return
     */
    public String embed(String msg, Set<String> fileIds) {
        log.debug("embedding... {}", msg);

        Set<String> finalFileIds = (fileIds == null) ? new HashSet<>() : fileIds;

        // 首先查询向量库
        String promptContent = vectorStore
                .similaritySearch(SearchRequest.query(msg).withTopK(5))
                .stream()
                .filter(doc -> {
                    if (CollectionUtils.isEmpty(finalFileIds)) return true;
                    Object fileIdObject = doc.getMetadata().get("file_id");
                    String docFileId = fileIdObject != null ? fileIdObject.toString() : null;
                    return finalFileIds.contains(docFileId);
                })
                .map(Document::getContent)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining(" "));

        // 确保 promptContent 不为空
        if (!StringUtils.hasText(promptContent)) {
            promptContent = "No relevant information found in the database.";
        }

        log.debug("Prompt content: {}", promptContent);

        return chatClient
                .prompt(promptContent)  //把 prompt 传递给大模型
                .user(msg)              //用户发送的内容
                .call()
                .content();
    }
}
