package com.mmk.etl.timer;

import com.mmk.etl.EtlConfig;
import com.mmk.etl.jpa.entity.EmbeddingLogEntity;
import com.mmk.etl.jpa.entity.FileUploadEntity;
import com.mmk.etl.jpa.service.FileBzService;
import com.mmk.etl.service.FileEtlService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 定时任务，当检测到新的文件时，自动执行嵌入，并存储到向量数据库。
 * TODO: 数据脱敏，防止信息泄露
 * TODO: 监控 file_upload 表，而不是监控目录
 * TODO: 完成文件上传接口
 * @author 大漠穷秋
 */
@Slf4j
@Service
@AllArgsConstructor
public class WatchFileTimer {

    private EtlConfig etlConfig;

    private FileEtlService fileEtlService;

    private FileBzService fileBzService;

    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    /**
     * 定时任务：扫描目录并处理新文件
     */
    @Scheduled(fixedRateString = "${application.watch-file.scan-interval}")
    public void watchAndProcessFiles() throws MalformedURLException, InterruptedException {
        if (!isRunning.compareAndSet(false, true)) {
            log.debug("监控文件，上一个定时任务还没有结束，跳过本次调度...");
            return;
        }

        log.debug("开始读取文件上传记录表...");

        Integer pageSize = etlConfig.getWatchMysql().getUserRowLimit();
        Integer pageCount = fileBzService.getAllFilePageCount(pageSize);

        try {
            for (int currentPage = 0; currentPage < pageCount; currentPage++) {
                log.debug("正在处理第 {} 页文件上传记录，共 {} 页...", currentPage + 1, pageCount);
                getAllFilePageable(currentPage, pageSize);
            }
        } finally {
            isRunning.set(false);
        }
    }

    private void getAllFilePageable(Integer currentPage, Integer size) {
        Optional.ofNullable(fileBzService.getAllFilePageable(currentPage, size))
                .filter(files -> !files.isEmpty())
                .ifPresentOrElse(
                    files -> files.forEach(this::processFileEntity),
                    () -> log.warn("分页查询结果为空，当前页: {}, 每页大小: {}", currentPage, size)
                );
    }

    /**
     * TODO: 测试，如果 PDF 文件体积非常大，例如 500M ，是否会出问题？
     * TODO: 统一异常处理
     * @param fileUploadEntity
     */
    private void processFileEntity(FileUploadEntity fileUploadEntity) {
        Path filePath = Paths.get(fileUploadEntity.getPath());
        File file = filePath.toFile();
        if (!file.exists() || !file.isFile()) {
            log.warn("文件不存在或不是有效文件: {}", fileUploadEntity.getPath());
            return;
        }

        Integer fileId = fileUploadEntity.getId();
        EmbeddingLogEntity embeddingLogEntity = fileBzService.getFileEmbeddingLog(fileId);
        if (embeddingLogEntity != null) {
            log.debug("文件已经处理过，跳过: {}", file.getAbsolutePath());
            return;
        }

        log.debug("开始处理文件: {}", file.getAbsolutePath());

        try {
            Resource resource = new UrlResource(file.toURI());

            List<Document> documents = fileEtlService.readFile(resource);

            log.debug("fileId 写入元数据...");
            documents.forEach(doc -> doc.getMetadata().put("file_id", fileId));

            log.debug("正在提取关键词...");
            documents = fileEtlService.keywordDocuments(documents);

            log.debug("正在生成摘要...");
            documents = fileEtlService.summaryDocuments(documents);

            log.debug("正在拆分文档...");
            documents = fileEtlService.splitDocument(documents);

            log.debug("正在保存文档...");
            documents = fileEtlService.saveDocument(documents);

            fileBzService.saveFileEmbeddingLog(fileId);

            log.info("文件处理完成: {}", file.getAbsolutePath());
        } catch (Exception e) {
            log.error("处理文件时发生异常: {}", file.getAbsolutePath(), e);
        }
    }
}
