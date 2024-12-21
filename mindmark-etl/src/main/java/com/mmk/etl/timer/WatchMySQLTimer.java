package com.mmk.etl.timer;

import com.mmk.etl.EtlConfig;
import com.mmk.etl.service.MySQLService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 定时任务，把数据库中的数据进行向量化。
 * TODO: 避免重复扫描表
 * TODO: 允许指定到字段级别
 * TODO: 分页查询
 * TODO: 数据脱敏，防止信息泄露
 * @author 大漠穷秋
 */
@Slf4j
@Service
@AllArgsConstructor
public class WatchMySQLTimer {
    private EtlConfig appConfig;

    private MySQLService mySQLService;

    /**
     * 定时任务：扫描指定的表，把表中的数据向量化。
     */
    @Scheduled(fixedRateString = "${application.watch-mysql.scan-interval}")
    public void watchAndProcessTables() throws InterruptedException {
        log.debug("Watching mysql database: ");

        String watchTable=appConfig.getWatchMysql().getTable();
        List<Map<String, Object>> dataList =mySQLService.executeQuery(watchTable);

        if (dataList == null || dataList.isEmpty()) {
            log.warn("No data found in table: {}", watchTable);
            return;
        }

        log.debug("Fetched {} records from table: {}", dataList.size(), watchTable);

        List<String> allResultString=new ArrayList<>();
        for (Map<String, Object> row : dataList) {
            StringBuilder rowString = new StringBuilder();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String columnName = entry.getKey();
                Object columnValue = entry.getValue();
                rowString.append(columnName)
                        .append(": ")
                        .append(columnValue != null ? columnValue.toString() : "NULL")
                        .append(", ");
            }

            if (rowString.length() > 0) {
                rowString.setLength(rowString.length() - 2);
            }

            log.debug("Row data: {}", rowString.toString());

            allResultString.add(rowString.toString());
        }

        log.debug("Finished processing {} records.", dataList.size());
        log.debug(String.join("", allResultString));

        //TODO: 把元数据写入 doc ，如数据库名、表名
        Document doc = new Document(String.join("", allResultString));
        List<Document> documents = new ArrayList<>();
        documents.add(doc);
        // NOTE: 提取摘要和关键词的处理速度非常慢
        documents = this.mySQLService.keywordDocuments(documents);
        documents = this.mySQLService.summaryDocuments(documents);
        documents = this.mySQLService.splitDocument(documents);
        documents = this.mySQLService.saveDocument(documents);
    }

}