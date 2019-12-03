package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.entity.DatabaseInfo;
import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.FileUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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

    public Database init(DatabaseInfo info) throws IOException, SQLException {
        this.info = info;
        this.connection = DbUtil.getConnection(info.getUrl(), info.getUsername(), info.getPassword());
        return configure();
    }

    private Database configure() throws IOException, SQLException {
        String sql = FileUtil.getStringByClasspath("sql/detail/database.sql");
        Database bean = DbUtil.getBean(this.connection, sql, Database.class, this.info.getName());
        bean.setTables(Table.configure(this.connection, this.info.getName()));
        bean.setInfo(this.info);
        bean.setConnection(this.connection);
        return bean;
    }

}
