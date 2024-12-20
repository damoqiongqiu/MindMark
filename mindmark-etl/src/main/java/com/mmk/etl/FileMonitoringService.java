package com.mmk.etl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定时任务，当检测到新的文件时，自动执行嵌入，并存储到向量数据库。
 * @author 大漠穷秋
 */
@Slf4j
@Service
@AllArgsConstructor
public class FileMonitoringService {

    private EtlService etlService;

    private final ApplicationConfig applicationConfig;

    // 记录已处理文件名，避免重复处理
    // TODO: 把已经处理过的文件记录到数据库
    private final Set<String> processedFiles = ConcurrentHashMap.newKeySet();

    /**
     * 定时任务：扫描目录并处理新文件
     * TODO: 监控任意层级结构，包括子目录
     */
    @Scheduled(fixedRateString = "${application.scan-interval}")
    public void monitorAndProcessFiles() throws MalformedURLException, InterruptedException {

        log.debug("-----------------------------------------");
        log.debug("开始扫描目录: " + applicationConfig.getUploadPath());
        log.debug("-----------------------------------------");

        File rootDir = new File(applicationConfig.getUploadPath());
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            log.warn("监控目录不存在: {}", applicationConfig.getUploadPath());
            return;
        }

        // 扫描所有子目录
        scanAndProcessFilesRecursively(rootDir);
    }

    private void scanAndProcessFilesRecursively(File dir) throws MalformedURLException, InterruptedException {
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            log.info("目录为空: {}", dir.getAbsolutePath());
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // 递归处理子目录
                scanAndProcessFilesRecursively(file);
            } else if (file.isFile() && !processedFiles.contains(file.getName())) {
                // 处理文件
                log.debug("开始处理文件: " + file.getAbsolutePath());

                Resource resource = new UrlResource(file.toURI());
                List<Document> documents = this.etlService.readFile(resource);

                // NOTE: 提取摘要和关键词的处理速度非常慢
                documents = this.etlService.keywordDocuments(documents);
                documents = this.etlService.summaryDocuments(documents);
                documents = this.etlService.splitDocument(documents);
                documents = this.etlService.saveDocument(documents);

                // 记录已经处理过的文件
                // TODO: 对文件进行 Hash 然后记录
                processedFiles.add(file.getName());
            }
        }
    }

}
