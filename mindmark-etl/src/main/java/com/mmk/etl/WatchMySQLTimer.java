package com.mmk.etl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

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
public class WatchMySQLTimer {
    private ApplicationConfig appConfig;

    /**
     * 定时任务：扫描指定的表，把表中的数据向量化。
     */
    @Scheduled(fixedRateString = "${application.watch-mysql.scan-interval}")
    public void watchAndProcessTables() {
        List<ApplicationConfig.WatchMysqlConfig.MysqlDatabaseConfig> databases = appConfig.getWatchMysql().getDatabases();
        for (ApplicationConfig.WatchMysqlConfig.MysqlDatabaseConfig database : databases) {
            String dbName = database.getDbName();
            List<String> tables = database.getTables();
            long scanInterval = database.getScanInterval();

            log.debug("Watching mysql database: " + dbName + " with tables: " + tables);

            // TODO: 开始扫描指定的表，进行向量化
        }
    }

}