package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.entity.DatabaseInfo;
import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.FileUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class Database {
    private DatabaseInfo info;
    private Connection connection;

    @DbUtilProperty("SCHEMA_NAME")
    private String name;
    @DbUtilProperty("DEFAULT_CHARACTER_SET_NAME")
    private String character;
    @DbUtilProperty("DEFAULT_COLLATION_NAME")
    private String collate;

    private List<Table> tables;
    private List<View> views;
    public static final String SQL = FileUtil.getStringByClasspath("sql/detail/database.sql");

    @JsonIgnore
    public List<Key> getKeyList() {
        List<Key> result = new ArrayList<>();
        this.getTables().forEach(s -> result.addAll(s.getKeys()));
        return result;
    }

    @JsonIgnore
    public List<Column> getColumnList() {
        List<Column> result = new ArrayList<>();
        this.getTables().forEach(s -> result.addAll(s.getColumns()));
        return result;
    }

    public Database init(DatabaseInfo info) throws SQLException {
        this.info = info;
        this.connection = DbUtil.getConnection(info.getUrl(), info.getUsername(), info.getPassword());
        return configure();
    }

    private Database configure() throws SQLException {
        Database bean = DbUtil.getBean(this.connection, SQL, Database.class, this.info.getName());
        bean.setTables(Table.configure(this.connection, this.info.getName()));
        bean.setViews(View.configure(this.connection, this.info.getName()));
        bean.setInfo(this.info);
        bean.setConnection(this.connection);
        return bean;
    }

}
