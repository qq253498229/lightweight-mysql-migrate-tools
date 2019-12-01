package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.entity.Database;
import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.FileUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
public class DatabaseStructure {
    private Database database;
    @DbUtilProperty("SCHEMA_NAME")
    private String name;
    @DbUtilProperty("DEFAULT_CHARACTER_SET_NAME")
    private String character;
    @DbUtilProperty("DEFAULT_COLLATION_NAME")
    private String collate;

    private List<TableStructure> tables;

    private Connection connection;

    public void configure(Database database) throws IOException, SQLException {
        this.database = database;
        this.connection = DbUtil.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
        configure();
    }

    private void configure() throws IOException, SQLException {
        String sql = FileUtil.getStringByClasspath("sql/database.sql");

        DatabaseStructure bean = DbUtil.getBean(this.connection, sql, DatabaseStructure.class, this.database.getName());
        Map<String, Object> structure = DbUtil.getDatabaseStructure(this.connection, this.database.getName());
        this.name = (String) structure.get("SCHEMA_NAME");
        this.character = (String) structure.get("DEFAULT_CHARACTER_SET_NAME");
        this.collate = (String) structure.get("DEFAULT_COLLATION_NAME");
        this.tables = TableStructure.configure(this.connection, this.name);
    }

}
