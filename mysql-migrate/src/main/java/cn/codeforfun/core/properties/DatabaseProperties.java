package cn.codeforfun.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wangbin
 */
@EnableConfigurationProperties
@Component
@ConfigurationProperties(prefix = "migrate")
@Getter
@Setter
public class DatabaseProperties {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String name;
}
