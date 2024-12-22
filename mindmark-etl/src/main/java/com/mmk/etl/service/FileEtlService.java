package com.mmk.etl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 大漠穷秋
 */
@Slf4j
@Service
public class FileEtlService extends EtlBaseService {
    /**
     * 全部使用 Tika 读取文件， Tika 支持大量的文件格式
     * https://tika.apache.org/3.0.0/formats.html
     * TODO: FIXME 文件的内容可能为空或无法解析
     * TODO: 提取原始文件名、创建时间、创建者等属性，写入 Document 的元数据中
     * @param resource
     * @return
     */
    public List<Document> readFile(Resource resource) {
        TikaDocumentReader tikaReader = new TikaDocumentReader(resource);
        return tikaReader.read();
    }
}
