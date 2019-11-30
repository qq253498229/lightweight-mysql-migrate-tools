package cn.codeforfun.config;

import cn.codeforfun.migrate.core.properties.DatabaseProperties;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author wangbin
 */
@Configuration
public class DatabaseConfig {
    @Resource
    private DatabaseProperties properties;

    @Bean
    public DataSource database() {
        DruidDataSource dataSource = new DruidDataSource();
        String url = "jdbc:mysql://" + properties.getHost() + ":" + properties.getPort() + "/" + properties.getName();
        url += "?useUnicode=true&characterEncoding=utf8&createDatabaseIfNotExist=true";
        dataSource.setUrl(url);
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        return dataSource;
    }
}
