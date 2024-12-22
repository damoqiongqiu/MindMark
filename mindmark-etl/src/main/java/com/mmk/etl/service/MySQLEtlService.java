package com.mmk.etl.service;

import com.mmk.etl.jpa.entity.DbEntity;
import com.mmk.etl.jpa.entity.SchemaEntity;
import com.mmk.etl.util.JdbcUrlBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MySQLEtlService extends EtlBaseService {

    /**
     * 由于需要根据用户的配置动态连接不同的数据库，这里手动创建 DataSource 。
     */
    public static DataSource createDataSource(DbEntity dbEntity, SchemaEntity schemaEntity) {
        String jdbcUrl= JdbcUrlBuilder.buildJdbcUrl(dbEntity,schemaEntity);
        String username=dbEntity.getUserName();
        String password=dbEntity.getPassword();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        //TODO: 需要测试并调整这些参数
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(30000);
        config.setLeakDetectionThreshold(2000);

        return new HikariDataSource(config);
    }
    
    /**
     * 根据用户的配置，到不同的数据库中执行查询，带分页。
     * @param tableName
     * @param startId
     * @param rowLimit
     * @return
     */
    public List<Map<String, Object>> executeQueryWithPagination(DbEntity dbEntity, SchemaEntity schemaEntity, String tableName, String idColumn, Integer startId, Integer rowLimit) {
        String nameRegex = "^[a-zA-Z0-9_-]{1,128}$";
        if (!Pattern.matches(nameRegex, tableName)) {
            log.error("Invalid table name: {}", tableName);
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }
        if (!Pattern.matches(nameRegex, idColumn)) {
            log.error("Invalid column name: {}", idColumn);
            throw new IllegalArgumentException("Invalid column name: " + idColumn);
        }
        
        JdbcTemplate jdbcTemplate = new JdbcTemplate(createDataSource(dbEntity,schemaEntity));
        //TODO: 这里需要测试，order by 操作对大数据量的 table 可能会产生显著的性能影响。
        String sql = String.format("SELECT * FROM %s WHERE %s >= ? ORDER BY %s ASC LIMIT ?", tableName, idColumn, idColumn);
        log.debug(sql);
        return jdbcTemplate.queryForList(sql, startId, rowLimit);
    }
}
