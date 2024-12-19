package com.mmk.llm.service.zhipu;

import com.mmk.llm.service.EmbeddingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 大漠穷秋
 */
@Slf4j
@Service
@AllArgsConstructor
public class ZhiPuEmbeddingService implements EmbeddingService {

    private final ChatClient chatClient;
    
    /**
     * TODO: 让用户上传文件，然后解析并写入向量数据库 。
     * TODO: 支持同时使用多种向量数据库。
     */
    private final VectorStore vectorStore;

    /**
     * 根据嵌入的文本进行查询
     * @param msg
     * @return
     */
    public String embed(String msg) {
        log.debug("embedding..." + msg);

        // 测试：向 ElasticSearch 中插入一些测试数据
        List<Document> documents = List.of(
                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
                new Document("The World is Big and Salvation Lurks Around the Corner"),
                new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2"))
        );

        // 添加到 Vector Store
        // 智谱大模型文档： https://bigmodel.cn/dev/api/vector/embedding
        // 输入需要向量化的文本，支持字符串数组。
        // embedding-2 的单条请求最多支持 512 个Tokens，数组总长度不得超过8K；
        // embedding-3 的单条请求最多支持 2048 个Tokens，数组总长度不得超过8K；
        // 且数组最大不得超过 64 条。
        vectorStore.add(documents);

        // 查询向量库
        List<Document> searchResults = vectorStore.similaritySearch(SearchRequest.query(msg).withTopK(5));
        List<String> contentList = searchResults.stream().map(Document::getContent).toList();
        String promptContent = String.join(" ", contentList);
        log.debug(promptContent);

        String result=chatClient
                .prompt(promptContent)   //把从向量数据库中查询到的内容作为 prompt 传递给模型
                .user(msg)               //用户的消息
                .call()
                .content();

        return result;
    }

}
