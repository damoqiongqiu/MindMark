package com.mmk.etl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
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
public class FileService extends EtlService{

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

}
