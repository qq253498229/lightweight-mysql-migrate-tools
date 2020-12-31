package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.entity.DatabaseInfo;
import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.FileUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@NoArgsConstructor
public class Database {
    private DatabaseInfo info;
    @JsonIgnore
    private Connection connection;

    @DbUtilProperty("SCHEMA_NAME")
    private String name;
    @DbUtilProperty("DEFAULT_CHARACTER_SET_NAME")
    private String character;
    @DbUtilProperty("DEFAULT_COLLATION_NAME")
    private String collate;
    /**
     * 包含的tables
     */
    private List<Table> tables;
    /**
     * 包含的views
     */
    private List<View> views;
    /**
     * 包含的functions
     */
    private List<Function> functions;
    /**
     * 包含的procedures
     */
    private List<Procedure> procedures;
    /**
     * 包含的triggers
     */
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
        bean.setTables(Table.configure(this.connection, this));
        bean.setViews(View.configure(this.connection, this));
        bean.setFunctions(Function.configure(this.connection, this));
        bean.setProcedures(Procedure.configure(this.connection, this));
        bean.setTriggers(Trigger.configure(this.connection, this));
        bean.setInfo(this.info);
        bean.setConnection(this.connection);
        return bean;
    }

}
