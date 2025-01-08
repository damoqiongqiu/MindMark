package com.mmk.etl.service;

import com.mmk.etl.jpa.entity.DbEntity;
import com.mmk.etl.jpa.entity.TableEntity;
import com.mmk.etl.util.JdbcUrlBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MySQLEtlService extends EtlBaseService {

    private static final String NAME_REGEX = "^[a-zA-Z0-9_-]{1,128}$";
    private static final ConcurrentHashMap<String, HikariDataSource> dataSourceCache = new ConcurrentHashMap<>();

    public MySQLEtlService(@Qualifier("zhiPuAiChatModel") ChatModel chatModel) {
        super(chatModel);
    }

    /**
     * 创建数据源，根据用户配置动态连接不同的数据库。
     */
    public static DataSource createDataSource(DbEntity dbEntity, TableEntity tableEntity) {
        String jdbcUrl = JdbcUrlBuilder.buildJdbcUrl(dbEntity, tableEntity);
        String username = dbEntity.getUserName();
        String password = dbEntity.getPassword();

        String cacheKey = jdbcUrl + username + password;
        return dataSourceCache.computeIfAbsent(cacheKey, key -> createHikariDataSource(jdbcUrl, username, password, cacheKey));
    }

    private static HikariDataSource createHikariDataSource(String jdbcUrl, String username, String password, String cacheKey) {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

            // 配置连接池参数
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(60000);
            config.setLeakDetectionThreshold(2000);

            log.info("创建新的数据源: {}", cacheKey);
            return new HikariDataSource(config);
        } catch (Exception e) {
            log.error("创建数据源时出错: {}", cacheKey, e);
            throw new RuntimeException("创建数据源时出错: " + cacheKey, e);
        }
    }

    /**
     * 执行带分页的查询，根据用户配置连接不同的数据库。
     * @return 查询结果
     */
    public List<Map<String, Object>> executeQueryWithPagination(DbEntity dbEntity, TableEntity tableEntity, Integer startId, Integer rowLimit) {
        validateName(tableEntity.getTableName(), "table name");
        validateName(tableEntity.getIdColumn(), "column name");

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(createDataSource(dbEntity, tableEntity));
            // 执行查询，按ID列排序并限制返回行数
            String sql = String.format("SELECT * FROM %s WHERE %s >= ? ORDER BY %s ASC LIMIT ?", tableEntity.getTableName(), tableEntity.getIdColumn(), tableEntity.getIdColumn());
            log.debug(sql);
            return jdbcTemplate.queryForList(sql, startId, rowLimit);
        } catch (Exception e) {
            log.error("执行带分页的查询时出错", e);
            throw new RuntimeException(e);
        }
    }

    private void validateName(String name, String type) {
        if (!Pattern.matches(NAME_REGEX, name)) {
            log.error("无效的 {}: {}", type, name);
            throw new IllegalArgumentException("无效的 " + type + ": " + name);
        }
    }
}
