package com.mmk.llm.service.zhipu;

import com.mmk.llm.service.EmbeddingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.ai.transformer.SummaryMetadataEnricher;
import org.springframework.ai.transformer.SummaryMetadataEnricher.SummaryType;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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
     * 智谱提供的文本嵌入模型
     * 输入和输出长度都有长度限制，参考官方文档：https://bigmodel.cn/dev/api/vector/embedding
     */
    private ZhiPuAiChatModel chatModel;
    
    /**
     * TODO: 让用户上传文件，然后解析并写入向量数据库 。
     * TODO: 支持同时使用多种向量数据库。
     */
    private final VectorStore vectorStore;

    /**
     * 上传文件
     * TODO: 自动检测文件类型并进行解析，文件解析器改成工厂模式
     * TODO: 文件解析时间一般都比较长，需要改成异步模式在后台进程处理
     * TODO: 重构代码到 mindmark-etl 模块中
     * TODO: 测试，如果 PDF 文件体积非常大，例如 500M ，是否会出问题？
     * TODO: 测试中文字符是否能解析成功
     */
    private List<Document> doUpload() throws IOException{
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("./pdf/*.pdf");
        List<Document> allDocs = new ArrayList<>();
        for (Resource pdfResource : resources) {
            log.info("Processing File: {}", pdfResource.getFilename());
            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource);
            List<Document> docs = new ArrayList<>();
            docs.addAll(pdfReader.read());
            if (docs.size()>0) {
                docs = summaryDocuments(keywordDocuments(docs));
            }
            allDocs.addAll(docs);
        }
        return allDocs;
    }

    /**
     * 写入摘要，不改变文件本身的内容，只向文件的元数据中写入摘要。
     * @param documents
     * @return
     */
    private List<Document> summaryDocuments(List<Document> documents) {
        log.info("提取摘要...");
        SummaryMetadataEnricher summaryEnricher =  new SummaryMetadataEnricher(chatModel,List.of(SummaryType.CURRENT));
        return summaryEnricher.apply(documents);
    }

    /**
     * 写入关键词，不改变文件本身的内容，只向文件的元数据中写入关键词。
     * @param documents
     * @return
     */
    private List<Document> keywordDocuments(List<Document> documents) {
        log.info("提取关键词...");
        KeywordMetadataEnricher keywordEnricher = new KeywordMetadataEnricher(chatModel, 50);
        return keywordEnricher.apply(documents);
    }

    /**
     * 分块
     * TODO: 测试中文分块的效果
     * TODO: 测试每个块的长度，尽量兼容不同的大模型
     * TODO: 重构代码到 mindmark-etl 模块中
     */
    private List<Document> doSplit(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter(512, 50, 64, 8000, true);
        return splitter.split(documents);
    }

    /**
     * 嵌入
     * TODO: 测试嵌入矩阵的维度参数，尽量兼容不同的大模型
     * TODO: 重构代码到 mindmark-etl 模块中
     */
    private void doEmbed(){

    }

    /**
     * 存储到某种向量数据库
     */
    private void doSave(){

    }

    /**
     * 根据嵌入的文本进行查询
     * TODO: 前端界面上让用户指定从单份文件，或者特定知识库进行相似性查询
     * @param msg
     * @return
     */
    public String embed(String msg) throws IOException, InterruptedException {
        log.debug("embedding..." + msg);

        // 测试：解析 PDF 文件
        List<Document> documents= doUpload();
        documents=doSplit(documents);

        // 测试：向 ElasticSearch 中插入一些测试数据
        // 添加到 Vector Store
        // 智谱大模型文档： https://bigmodel.cn/dev/api/vector/embedding
        // 输入需要向量化的文本，支持字符串数组。
        // embedding-2 的单条请求最多支持 512 个Tokens，数组总长度不得超过8K；
        // embedding-3 的单条请求最多支持 2048 个Tokens，数组总长度不得超过8K；
        // 且数组最大不得超过 64 条。
        log.debug("文件解析成功，开始写入 ElasticSearch...");
        vectorStore.add(documents);

        //TODO: 强制 ElasticSearch 立即刷新索引
        Thread.sleep(3000);

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
