package com.mmk.llm.service.zhipu;

import com.mmk.llm.service.EmbeddingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.elasticsearch.ElasticsearchVectorStore;
import org.springframework.ai.zhipuai.ZhiPuAiEmbeddingModel;
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

    /**
     * 智谱提供的文本嵌入模型
     * 输入和输出长度都有长度限制，参考官方文档：https://bigmodel.cn/dev/api/vector/embedding
     */
    private final ZhiPuAiEmbeddingModel embeddingModel;

    /**
     * Elasticsearch 向量数据库
     * TODO: 让用户上传文件，然后解析并写入 ElasticSearch 。
     */
    private final ElasticsearchVectorStore vectorStore;

    public EmbeddingResponse embed(String msg) {
        log.debug("embedding..." + msg);

        // 测试：向 ElasticSearch 中插入一些测试数据
        List<Document> documents = List.of(
                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
                new Document("The World is Big and Salvation Lurks Around the Corner"),
                new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2"))
        );

        // 添加到 Vector Store
        vectorStore.add(documents);

        // 测试查询
        List<Document> results = this.vectorStore.similaritySearch(
                SearchRequest.query(msg).withTopK(5)
        );

        log.debug(results.toString());

        // 提取结果中的内容字符串
        List<String> contentList = results
                .stream()
                .map(Document::getContent)
                .toList();

        return this.embeddingModel.embedForResponse(contentList);
    }

}
