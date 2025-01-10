package com.mmk.etl.service;

import com.mmk.etl.utils.TextSplitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.ai.transformer.SummaryMetadataEnricher;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

/**
 * ETL 相关的服务，用来切片、嵌入、存储
 * @author 大漠穷秋
 */
@Slf4j
public abstract class EtlBaseService {
    //TODO: 这里似乎应该默认选择 OpenAiChatModel ，因为绝大多数模型会选择兼容 OpenAi
    @Autowired
    @Qualifier("zhiPuAiChatModel")
    protected ChatModel chatModel;

    @Autowired
    protected VectorStore vectorStore;

    /**
     * 分割文档
     */
    public List<Document> splitDocuments(List<Document> documents) {
        List<Document> result = new ArrayList<>();
        for (Document doc : documents) {
            List<String> chunks = TextSplitter.splitText(doc.getText());
            for (String chunk : chunks) {
                Document newDoc = new Document(chunk);
                newDoc.getMetadata().putAll(doc.getMetadata());
                result.add(newDoc);
            }
        }
        return result;
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
     * 存储到某种向量数据库。
     * 底层会调用 EmbeddingModel 的 API ，嵌入操作可能会失败，需要处理异常情况，例如费用不够。
     * 不同的 EmbeddingModel 可能需要不同的参数，这里需要采用能够兼容不同模型的写法。
     * 智谱大模型文档： https://bigmodel.cn/dev/api/vector/embedding
     * embedding-2 的单条请求最多支持 512 个Tokens，数组总长度不得超过8K；
     * embedding-3 的单条请求最多支持 2048 个Tokens，数组总长度不得超过8K；
     * 且数组最大不得超过 64 条。
     */
    public List<Document> addDocument(List<Document> documents) {
        log.debug("开始写入...");

        vectorStore.add(documents);

        //TODO: 强制 ElasticSearch 立即刷新索引
        
        return documents;
    }
}
