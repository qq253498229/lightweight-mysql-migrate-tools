package cn.codeforfun.migrate.core.entity;

import cn.codeforfun.migrate.core.utils.ObjectUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

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
     * 数据库用户名
     */
    private String username;
    /**
     * 数据库密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    /**
     * 数据库名
     */
    private String name;
    /**
     * 数据库连接地址
     */
    private String url;
    /**
     * 忽略编码及字符集对比
     */
    private Boolean ignoreCharacterCompare = false;
    /**
     * 连接时区
     */
    private String timezone;

    /**
     * @param host     数据库地址
     * @param port     数据库端口
     * @param username 数据库用户名
     * @param password 数据库密码
     * @param name     数据库名
     */
    public DatabaseInfo(String host, Integer port, String username, String password, String name) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.name = name;
        this.url = getUrl();
    }

    /**
     * 拼接数据库连接地址
     *
     * @return 数据库连接地址
     */
    public String getUrl() {
        if (!ObjectUtils.isEmpty(this.url)) {
            return this.url;
        }
        String url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + name;
        url += "?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";
        String timeZone;
        if (ObjectUtils.isEmpty(this.timezone)) {
            timeZone = "GMT%2B8";
        } else {
            timeZone = this.timezone;
        }
        url += "&serverTimezone=" + timeZone;
        return url;
    }
}
