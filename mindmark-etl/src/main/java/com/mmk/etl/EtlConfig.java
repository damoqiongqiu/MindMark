package com.mmk.etl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class EtlConfig {

    // watch-file 配置
    private WatchFileConfig watchFile;

    // watch-mysql 配置
    private WatchMysqlConfig watchMysql;

    @Getter
    @Setter
    public static class WatchFileConfig {
        private boolean enabled;
        private String filePath;
        private Long scanInterval;
    }

    @Getter
    @Setter
    public static class WatchMysqlConfig {
        private boolean enabled;
        private Long scanInterval;
        private Integer dataRowLimit;
        private Integer fileRowLimit;
        private Integer userRowLimit;
    }
}
