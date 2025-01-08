package com.mmk.etl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.ai.transformer.SummaryMetadataEnricher;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ETL 相关的服务，用来切片、嵌入、存储
 * @author 大漠穷秋
 */
@Slf4j
@Service
public class EtlBaseService {
    private final ChatModel chatModel;

    public EtlBaseService(@Qualifier("zhiPuAiChatModel") ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Autowired
    protected VectorStore vectorStore;

    /**
     * 把 Document 分割成小块
     * TODO: 测试中文分块的效果
     * TODO: 测试每个块的长度，尽量兼容不同的大模型
     */
    public List<Document> splitDocument(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter(512, 50, 64, 8000, true);
        return splitter.split(documents);
    }

    /**
     * 写入摘要，不改变文件本身的内容，只向文件的元数据中写入摘要。
     * @param documents
     * @return
     */
    public List<Document> summaryDocuments(List<Document> documents) {
        log.info("开始提取摘要...");
        SummaryMetadataEnricher summaryEnricher =  new SummaryMetadataEnricher(chatModel,List.of(SummaryMetadataEnricher.SummaryType.CURRENT));
        return summaryEnricher.apply(documents);
    }

    /**
     * 写入关键词，不改变文件本身的内容，只向文件的元数据中写入关键词。
     * @param documents
     * @return
     */
    public List<Document> keywordDocuments(List<Document> documents) {
        log.info("开始提取关键词...");
        KeywordMetadataEnricher keywordEnricher = new KeywordMetadataEnricher(chatModel, 50);
        return keywordEnricher.apply(documents);
    }

    /**
     * 存储到某种向量数据库
     */
    public List<Document> saveDocument(List<Document> documents) throws InterruptedException {
        log.debug("开始写入...");

        // 智谱大模型文档： https://bigmodel.cn/dev/api/vector/embedding
        // embedding-2 的单条请求最多支持 512 个Tokens，数组总长度不得超过8K；
        // embedding-3 的单条请求最多支持 2048 个Tokens，数组总长度不得超过8K；
        // 且数组最大不得超过 64 条。
        // TODO: 底层会调用嵌入模型的 API ，嵌入操作可能会失败，需要处理异常情况，例如费用不够。
        vectorStore.add(documents);

        //TODO: 强制 ElasticSearch 立即刷新索引
        
        return documents;
    }
}
