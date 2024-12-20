package com.mmk.etl.timer;

import com.mmk.etl.EtlConfig;
import com.mmk.etl.service.FileService;
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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 定时任务，当检测到新的文件时，自动执行嵌入，并存储到向量数据库。
 * TODO: 数据脱敏，防止信息泄露
 * @author 大漠穷秋
 */
@Slf4j
@Service
@AllArgsConstructor
public class WatchFileTimer {

    private EtlConfig appConfig;

    private FileService fileService;

    // 记录已处理文件名，避免重复处理
    // TODO: 把已经处理过的文件记录到数据库
    private final Set<String> processedFiles = ConcurrentHashMap.newKeySet();

    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    /**
     * 定时任务：扫描目录并处理新文件
     * TODO: 监控任意层级结构，包括子目录
     */
    @Scheduled(fixedRateString = "${application.watch-file.scan-interval}")
    public void watchAndProcessFiles() throws MalformedURLException, InterruptedException {
        if (!isRunning.compareAndSet(false, true)) {
            return;
        }

        log.debug("----------------------------------------------------------------------------------");
        log.debug("Watching file: "+appConfig.getWatchFile().getFilePath());
        log.debug("----------------------------------------------------------------------------------");

        try {
            File rootDir = new File(appConfig.getWatchFile().getFilePath());
            if (!rootDir.exists() || !rootDir.isDirectory()) {
                log.warn("监控目录不存在: {}", appConfig.getWatchFile().getFilePath());
                return;
            }
            scanAndProcessFilesRecursively(rootDir);
        } finally {
            isRunning.set(false);
        }
    }

    /**
     * 递归处理子目录
     * @param dir
     * @throws MalformedURLException
     * @throws InterruptedException
     */
    private void scanAndProcessFilesRecursively(File dir) throws MalformedURLException, InterruptedException {
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            log.info("目录为空: {}", dir.getAbsolutePath());
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                scanAndProcessFilesRecursively(file);
            } else if (file.isFile() && !processedFiles.contains(file.getName())) {
                log.debug("开始处理文件: " + file.getAbsolutePath());

                Resource resource = new UrlResource(file.toURI());
                List<Document> documents = this.fileService.readFile(resource);

                // NOTE: 提取摘要和关键词的处理速度非常慢
                documents = this.fileService.keywordDocuments(documents);
                documents = this.fileService.summaryDocuments(documents);
                documents = this.fileService.splitDocument(documents);
                documents = this.fileService.saveDocument(documents);

                // 记录已经处理过的文件
                // TODO: 对文件进行 Hash 然后记录
                processedFiles.add(file.getName());
            }
        }
    }

}
