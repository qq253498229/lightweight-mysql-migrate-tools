package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.FileUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表定义
 *
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
public class TableStructure implements Serializable {
    private static final long serialVersionUID = 411108952654575238L;

    @DbUtilProperty("TABLE_SCHEMA")
    private String schema;
    @DbUtilProperty("TABLE_NAME")
    private String name;
    @DbUtilProperty("TABLE_TYPE")
    private String type;
    @DbUtilProperty("ENGINE")
    private String engine;
    @DbUtilProperty("CREATE_TIME")
    private Date createTime;
    @DbUtilProperty("UPDATE_TIME")
    private Date updateTime;
    @DbUtilProperty("CHARACTER_SET_NAME")
    private String charset;
    @DbUtilProperty("TABLE_COLLATION")
    private String collate;
    @DbUtilProperty("TABLE_COMMENT")
    private String comment;

    private List<ColumnStructure> columns;
    private List<KeyStructure> keys;

    public static List<TableStructure> configure(Connection connection, String databaseName) throws IOException, SQLException {
        List<TableStructure> list1 = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/table.sql"),
                TableStructure.class, databaseName);
        List<ColumnStructure> list2 = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/column.sql"),
                ColumnStructure.class, databaseName);
        List<KeyStructure> list3 = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/key.sql"),
                KeyStructure.class, databaseName);
        return list1.stream().peek(o -> {
            o.setColumns(list2.stream().filter(s -> o.getName().equals(s.getTable())).collect(Collectors.toList()));
            o.setKeys(list3.stream().filter(s -> o.getName().equals(s.getTableName())).collect(Collectors.toList()));
        }).collect(Collectors.toList());
    }
}
