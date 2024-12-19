package com.mmk.etl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.ai.transformer.SummaryMetadataEnricher;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ETL 相关的服务，用来读取文件、切片、嵌入、存储
 * @author 大漠穷秋
 */
@Slf4j
@Service
@AllArgsConstructor
public class EtlService {

    private ChatModel chatModel;

    /**
     * TODO: 让用户上传文件，然后解析并写入向量数据库 。
     * TODO: 支持同时使用多种向量数据库。
     */
    public final VectorStore vectorStore;

    /**
     * 根据通配符表达式，读取某个某种的文件列表，例如： "./files/*.files" ，将会读取 files 目录下的所有 .files 文件。
     * TODO: 测试，如果 PDF 文件体积非常大，例如 500M ，是否会出问题？
     * TODO: 统一异常处理
     *
     * @param locationPattern
     * @return
     * @throws IOException
     */
    public List<Document> readFileList(String locationPattern) throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(locationPattern);
        List<Document> allDocs = new ArrayList<>();
        for (Resource resource : resources) {
            allDocs.addAll(this.readFile(resource));
        }
        return allDocs;
    }

    /**
     * 全部使用 Tika 读取文件， Tika 支持大量的文件格式
     * https://tika.apache.org/3.0.0/formats.html
     * TODO: FIXME 文件的内容可能为空或无法解析
     * @param resource
     * @return
     */
    public List<Document> readFile(Resource resource) {
        TikaDocumentReader tikaReader = new TikaDocumentReader(resource);
        return tikaReader.read();
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
     * 把 Document 分割成小块
     * TODO: 测试中文分块的效果
     * TODO: 测试每个块的长度，尽量兼容不同的大模型
     */
    public List<Document> splitDocument(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter(512, 50, 64, 8000, true);
        return splitter.split(documents);
    }

    /**
     * 存储到某种向量数据库
     */
    public List<Document> saveDocument(List<Document> documents) throws InterruptedException {
        log.debug("开始写入...");

        // 测试：向 ElasticSearch 中插入一些测试数据
        // 智谱大模型文档： https://bigmodel.cn/dev/api/vector/embedding
        // 输入需要向量化的文本，支持字符串数组。
        // embedding-2 的单条请求最多支持 512 个Tokens，数组总长度不得超过8K；
        // embedding-3 的单条请求最多支持 2048 个Tokens，数组总长度不得超过8K；
        // 且数组最大不得超过 64 条。
        // TODO: 底层会调用嵌入模型的 API ，嵌入操作可能会失败，需要处理异常情况，例如费用不够。
        vectorStore.add(documents);

        //TODO: 强制 ElasticSearch 立即刷新索引
        Thread.sleep(3000);

        return documents;
    }
}
