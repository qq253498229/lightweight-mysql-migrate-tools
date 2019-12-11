package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.entity.DatabaseInfo;
import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.FileUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据库结构定义
 *
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
    private List<Function> functions;
    private List<Trigger> triggers;

    public static final String SQL = FileUtil.getStringByClasspath("sql/detail/database.sql");

    /**
     * 初始化数据库结构
     *
     * @param info 数据库基本信息
     * @return 数据库结构
     * @throws SQLException SQL异常
     */
    public Database init(DatabaseInfo info) throws SQLException {
        this.info = info;
        this.connection = DbUtil.getConnection(info.getUrl(), info.getUsername(), info.getPassword());
        return configure();
    }

    /**
     * 配置数据库结构
     *
     * @return 数据库结构
     * @throws SQLException SQL异常
     */
    private Database configure() throws SQLException {
        Database bean = DbUtil.getBean(this.connection, SQL, Database.class, this.info.getName());
        bean.setTables(Table.configure(this.connection, this.info.getName()));
        bean.setViews(View.configure(this.connection, this.info.getName()));
        bean.setFunctions(Function.configure(this.connection, this.info.getName()));
        bean.setTriggers(Trigger.configure(this.connection, this.info.getName()));
        bean.setInfo(this.info);
        bean.setConnection(this.connection);
        return bean;
    }

}
