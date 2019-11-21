package cn.codeforfun.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wangbin
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Database {
    private String host;
    private Integer port;
    private String table;
    private String username;
    private String password;
    private String url;

    public Database(String host, Integer port, String table, String username, String password) {
        this.host = host;
        this.port = port;
        this.table = table;
        this.username = username;
        this.password = password;
    }

    public String getUrl() {
        return "jdbc:mysql://" + this.host + ":" + this.port + "/" + table;
    }
}
