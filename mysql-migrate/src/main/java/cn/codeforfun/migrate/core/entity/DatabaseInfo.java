package cn.codeforfun.migrate.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.TimeZone;

/**
 * 数据库基本信息
 *
 * @author wangbin
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseInfo implements Serializable {
    private static final long serialVersionUID = -4275505729761182481L;
    /**
     * 数据库地址
     */
    private String host;
    /**
     * 数据库端口
     */
    private Integer port;
    /**
     * 数据库名
     */
    private String name;
    /**
     * 数据库用户名
     */
    private String username;
    /**
     * 数据库密码
     */
    private String password;
    /**
     * 数据库连接地址
     */
    private String url;

    /**
     * @param host     数据库地址
     * @param port     数据库端口
     * @param name     数据库名
     * @param username 数据库用户名
     * @param password 数据库密码
     */
    public DatabaseInfo(String host, Integer port, String name, String username, String password) {
        this.host = host;
        this.port = port;
        this.name = name;
        this.username = username;
        this.password = password;
        this.url = getUrl();
    }

    /**
     * 拼接数据库连接地址
     *
     * @return 数据库连接地址
     */
    public String getUrl() {
        String url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + name;
        url += "?useUnicode=true&characterEncoding=utf8&createDatabaseIfNotExist=true";
        String timeZone = TimeZone.getDefault().getID();
        if (!ObjectUtils.isEmpty(timeZone)) {
            url += "&serverTimezone=" + timeZone;
        }
        return url;
    }
}
