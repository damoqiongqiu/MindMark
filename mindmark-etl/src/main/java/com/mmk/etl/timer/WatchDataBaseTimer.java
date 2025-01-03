package com.mmk.etl.timer;

import com.mmk.etl.EtlConfig;
import com.mmk.etl.jpa.entity.*;
import com.mmk.etl.jpa.service.DataBaseBzService;
import com.mmk.etl.service.MySQLEtlService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 定时任务，把数据库中的数据进行向量化。
 * TODO: 允许指定到字段级别
 * TODO: 分页查询
 * TODO: 数据脱敏，防止信息泄露
 * @author 大漠穷秋
 */
@Slf4j
@Service
@AllArgsConstructor
public class WatchDataBaseTimer {

    private DataBaseBzService dataBaseBzService;

    private EtlConfig etlConfig;

    private MySQLEtlService mySQLEtlService;

    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    @Scheduled(fixedRateString = "${application.watch-mysql.scan-interval}")
    public void watchAndProcessTables() {
        if (!isRunning.compareAndSet(false, true)) {
            log.debug("监控数据库表，上一个定时任务还没有结束，跳过本次调度...");
            return;
        }

        log.debug("开始读取 MySQL 数据库表...");

        Integer pageSize = etlConfig.getWatchMysql().getUserRowLimit();
        Integer pageCount = dataBaseBzService.getAllUserPageCount(pageSize);

        try {
            for (int currentPage = 0; currentPage < pageCount; currentPage++) {
                log.debug("正在处理第 {} 页用户，共 {} 页...", currentPage + 1, pageCount);
                getAllUserPageable(currentPage, pageSize);
            }
        } finally {
            isRunning.set(false);
        }
    }

    private void getAllUserPageable(Integer currentPage, Integer size) {
        Page<UserEntity> users = dataBaseBzService.getAllUserPageable(currentPage, size);
        users.forEach(this::processUserEntity);
    }

    private void processUserEntity(UserEntity userEntity) {
        List<DbEntity> dbEntityList = dataBaseBzService.getDbEntityListByUserId(userEntity.getUserId());
        dbEntityList.forEach(this::processDbEntity);
    }

    private void processDbEntity(DbEntity dbEntity) {
        Integer dbId = dbEntity.getId();
        List<SchemaEntity> schemaEntityList = dataBaseBzService.getSchemaEntityListByDbId(dbId);
        schemaEntityList.forEach(schemaEntity -> processSchemaEntity(schemaEntity, dbEntity));
    }

    private void processSchemaEntity(SchemaEntity schemaEntity, DbEntity dbEntity) {
        Integer schemaId = schemaEntity.getId();
        List<TableEntity> tableEntityList = dataBaseBzService.getTableEntityListBySchemaId(schemaId);
        tableEntityList.forEach(tableEntity -> processTableEntity(tableEntity, dbEntity, schemaEntity));
    }

    private void processTableEntity(TableEntity tableEntity, DbEntity dbEntity, SchemaEntity schemaEntity) {
        Integer dbId = dbEntity.getId();
        Integer tableId = tableEntity.getId();
        String tableName = tableEntity.getTableName();
        String idColumn = tableEntity.getIdColumn();
        Integer dataRowLimit = etlConfig.getWatchMysql().getDataRowLimit();

        //NOTE: 查询上一次处理到了表中的哪一行数据
        EmbeddingLogEntity embeddingLogEntity = dataBaseBzService.getDbEmbeddingLog(dbId, tableId);
        Integer startId = Optional.ofNullable(embeddingLogEntity)
                                  .map(EmbeddingLogEntity::getStartId)
                                  .orElse(0);

        try {
            while (true) {
                List<Map<String, Object>> dataList = mySQLEtlService.executeQueryWithPagination(dbEntity, schemaEntity, tableName, idColumn, startId, dataRowLimit);
                if (CollectionUtils.isEmpty(dataList)) {
                    log.debug("表 {} 中没有更多记录要处理，结束查询。", tableName);
                    break;
                }

                log.debug("从表 {} 中获取了 {} 条记录，startId: {}", tableName, dataList.size(), startId);

                embedDataList(dataList);

                //TODO: 无自增主键怎么办？
                startId = dataList.stream()
                                  .map(data -> (Integer) data.get(idColumn))
                                  .max(Integer::compareTo)
                                  .orElse(0) + 1;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            dataBaseBzService.saveDbEmbeddingLog(dbId,tableId,startId);
        }
    }

    /**
     * 工具方法，把获取到的结果集整理成字符串。
     */
    private List<Document> embedDataList(List<Map<String, Object>> dataList) throws InterruptedException {
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
        documents = mySQLEtlService.keywordDocuments(documents);
        documents = mySQLEtlService.summaryDocuments(documents);
        documents = mySQLEtlService.splitDocument(documents);
        documents = mySQLEtlService.saveDocument(documents);
        return documents;
    }

}