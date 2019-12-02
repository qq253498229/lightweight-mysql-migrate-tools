package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.entity.Database;
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
public class DatabaseStructure {
    private Database database;
    private Connection connection;

    @DbUtilProperty("SCHEMA_NAME")
    private String name;
    @DbUtilProperty("DEFAULT_CHARACTER_SET_NAME")
    private String character;
    @DbUtilProperty("DEFAULT_COLLATION_NAME")
    private String collate;

    private List<TableStructure> tables;

    public DatabaseStructure init(Database database) throws IOException, SQLException {
        this.database = database;
        this.connection = DbUtil.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
        return configure();
    }

    private DatabaseStructure configure() throws IOException, SQLException {
        String sql = FileUtil.getStringByClasspath("sql/database.sql");
        DatabaseStructure bean = DbUtil.getBean(this.connection, sql, DatabaseStructure.class, this.database.getName());
        bean.setTables(TableStructure.configure(this.connection, this.database.getName()));
        bean.setDatabase(this.database);
        bean.setConnection(this.connection);
        return bean;
    }

}
