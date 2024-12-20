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
     * TODO: 前端界面上让用户指定从单份文件，或者特定知识库进行相似性查询
     * @param msg
     * @return
     */
    public String embed(String msg) {
        log.debug("embedding..." + msg);

        // 首先查询向量库
        List<Document> searchResults = vectorStore.similaritySearch(SearchRequest.query(msg).withTopK(5));
        List<String> contentList = searchResults.stream().map(Document::getContent).toList();
        String promptContent = String.join(" ", contentList);
        log.debug(promptContent);

        // 调用大模型生成答案
        String result=chatClient
                .prompt(promptContent)   //把从向量数据库中查询到的内容作为 prompt 传递给模型
                .user(msg)               //用户的消息
                .call()
                .content();

        return result;
    }

}
