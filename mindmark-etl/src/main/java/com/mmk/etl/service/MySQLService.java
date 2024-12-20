package com.mmk.etl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MySQLService extends EtlService{

    @Autowired
    protected DataSource dataSource;

    /**
     * 获取数据库连接
     */
    protected JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    /**
     * 执行查询操作，获取指定表的数据
     * TODO: 动态配置需要向量化的数据库、表、字段
     * @return 查询结果
     */
    public List<Map<String, Object>> executeQuery(String tableName) {
        String tableNameRegex = "^[a-zA-Z0-9_-]+$";

        if (!Pattern.matches(tableNameRegex, tableName)) {
            log.error("Invalid table name: {}", tableName);
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }

        String sql = "SELECT * FROM " + tableName;

        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        try {
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            log.debug("Fetched data from table " + tableName + " with " + results.size() + " records.");
            return results;
        } catch (Exception e) {
            log.error("Error querying table: " + tableName, e);
        }
        return null;
    }

}
