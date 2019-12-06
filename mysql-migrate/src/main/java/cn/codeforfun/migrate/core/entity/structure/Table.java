package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.diff.Difference;
import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.FileUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static cn.codeforfun.migrate.core.entity.structure.Key.FLAG_PRIMARY;

/**
 * 表结构定义
 *
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
public class Table implements Serializable, Difference {
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

    private List<Column> columns;
    private List<Key> keys;

    public static final String SQL = FileUtil.getStringByClasspath("sql/diff/create-table.sql");

    public String getDeleteForeignKeySql() {
        StringBuilder sb = new StringBuilder();
        for (Key key : this.keys) {
            if (!FLAG_PRIMARY.equals(key.getName())
                    && !ObjectUtils.isEmpty(key.getReferencedSchema())
                    && !ObjectUtils.isEmpty(key.getReferencedTable())
                    && !ObjectUtils.isEmpty(key.getReferencedColumn())
            ) {
                sb.append("ALTER TABLE `").append(key.getTableName()).append("` DROP FOREIGN KEY `").append(key.getName()).append("`;\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String getUpdateSql() {
        return null;
    }

    @Override
    public String getDeleteSql() {
        return "DROP TABLE `" + this.name + "`;";
    }

    @Override
    public String getCreateSql() {
        String sql = SQL;
        sql = sql.replace("${tableName}", this.name);
        sql = sql.replace("${engine}", " ENGINE = " + this.engine);
        sql = sql.replace("${charset}", " DEFAULT CHARSET = " + this.charset);
        sql = sql.replace("${collate}", " COLLATE = " + this.collate);
        sql = sql.replace("${comment}", ObjectUtils.isEmpty(this.comment) ? "" : " COMMENT = '" + this.comment + "'");
        StringBuilder sb = new StringBuilder();
        for (Column column : this.columns) {
            String columnSql = column.getCreateTableSql();
            sb.append(columnSql);
        }
        for (Key key : this.keys) {
            String keySql = key.getCreateTableSql();
            sb.append(keySql);
        }
        String columnSql = sb.substring(0, sb.length() - 1);
        sql = sql.replace("${columnSql}", columnSql);
        return sql;
    }

    public static List<Table> configure(Connection connection, String databaseName) throws SQLException {
        List<Table> list1 = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/table.sql"),
                Table.class, databaseName);
        List<Column> list2 = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/column.sql"),
                Column.class, databaseName);
        List<Key> list3 = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/key.sql"),
                Key.class, databaseName);
        return list1.stream().peek(o -> {
            o.setColumns(list2.stream().filter(s -> o.getName().equals(s.getTable())).collect(Collectors.toList()));
            o.setKeys(list3.stream().filter(s -> o.getName().equals(s.getTableName())).collect(Collectors.toList()));
        }).collect(Collectors.toList());
    }

}
