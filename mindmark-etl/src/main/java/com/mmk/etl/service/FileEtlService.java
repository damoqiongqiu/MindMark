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
     * 使用 Apache Tika 读取各种格式的文件并转换为 Document 对象列表。
     * 支持的文件格式包括但不限于：
     * - Microsoft Office (doc, docx, xls, xlsx, ppt, pptx)
     * - OpenDocument Format (odt, ods, odp)
     * - PDF文档
     * - 纯文本文件 (txt, csv, json, xml)
     * - HTML/网页文件
     * - 图片中的文字 (需要配置 Tesseract OCR)
     * - 音频语音（需要配置语音识别引擎）
     *
     * @param resource 要读取的文件资源，通常是本地文件系统中的文件
     * @return 返回从文件中提取的文档列表，每个Document对象包含文本内容和元数据
     * @see org.springframework.ai.document.Document
     * @see <a href="https://tika.apache.org/3.0.0/formats.html">Apache Tika支持的文件格式</a>
     */
    public List<Document> readFile(Resource resource) {
        TikaDocumentReader tikaReader = new TikaDocumentReader(resource);
        return tikaReader.read();
    }
}
