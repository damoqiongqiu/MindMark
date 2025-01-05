package com.mmk.etl.util;

import com.mmk.etl.jpa.entity.DbEntity;
import com.mmk.etl.jpa.entity.TableEntity;

/**
 * 拼接数据库连接 URL
 * @author 大漠穷秋
 */

public class JdbcUrlBuilder {

    public static String buildJdbcUrl(DbEntity dbEntity, TableEntity tableEntity) {
        if (dbEntity == null || tableEntity == null) {
            throw new IllegalArgumentException("DbEntity or SchemaEntity is null");
        }

        String dbType = dbEntity.getDbType();
        String ip = dbEntity.getIp();
        Integer port = dbEntity.getPort();
        String charset = dbEntity.getCharset();
        String userName = dbEntity.getUserName();
        String password = dbEntity.getPassword();
        String schemaName = tableEntity.getSchemaName();

        switch (dbType.toLowerCase()) {
            case "mysql":
                return buildMySqlUrl(ip, port, charset, schemaName, userName, password);
            case "postgresql":
                return buildPostgresUrl(ip, port, schemaName, userName, password);
            case "oracle":
                return buildOracleUrl(ip, port, schemaName, userName, password);
            default:
                throw new UnsupportedOperationException("Unsupported database type: " + dbType);
        }
    }

    private static String buildMySqlUrl(String ip, Integer port, String charset, String schemaName, String userName, String password) {
        if (charset == null || charset.isEmpty()) {
            charset = "utf8";
        }
        return String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=%s&serverTimezone=GMT%%2B8&useSSL=false",
                ip, port, schemaName, charset);
    }

    private static String buildPostgresUrl(String ip, Integer port, String schemaName, String userName, String password) {
        return String.format("jdbc:postgresql://%s:%d/%s", ip, port, schemaName);
    }

    private static String buildOracleUrl(String ip, Integer port, String schemaName, String userName, String password) {
        return String.format("jdbc:oracle:thin:@%s:%d:%s", ip, port, schemaName);
    }
}