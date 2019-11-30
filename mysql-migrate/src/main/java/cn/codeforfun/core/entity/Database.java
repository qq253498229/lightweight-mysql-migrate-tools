package cn.codeforfun.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wangbin
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Database implements Serializable {
    private static final long serialVersionUID = -4275505729761182481L;
    private String host;
    private Integer port;
    private String name;
    private String username;
    private String password;
    private String url;

    /**
     * @param host     数据库地址
     * @param port     数据库端口
     * @param name     数据库名
     * @param username 数据库用户名
     * @param password 数据库密码
     */
    public Database(String host, Integer port, String name, String username, String password) {
        this.host = host;
        this.port = port;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getUrl() {
        String url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + name;
        url += "?useUnicode=true&characterEncoding=utf8&createDatabaseIfNotExist=true";
        return url;
    }
}
