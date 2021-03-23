package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.diff.Difference;
import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.FileUtil;
import cn.codeforfun.migrate.core.utils.ObjectUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
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
    private Timestamp createTime;
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

    private Database database;

    public static final String SQL = FileUtil.getStringByClasspath("sql/diff/create-table.sql");

    public boolean hasForeignKey() {
        for (Key key : this.keys) {
            if (!FLAG_PRIMARY.equals(key.getName())
                    && !ObjectUtils.isEmpty(key.getReferencedSchema())
                    && !ObjectUtils.isEmpty(key.getReferencedTable())
                    && !ObjectUtils.isEmpty(key.getReferencedColumn())
            ) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public String getDeleteForeignKeySql() {
        StringBuilder sb = new StringBuilder();
        for (Key key : this.keys) {
            if (!FLAG_PRIMARY.equals(key.getName())
                    && !ObjectUtils.isEmpty(key.getReferencedSchema())
                    && !ObjectUtils.isEmpty(key.getReferencedTable())
                    && !ObjectUtils.isEmpty(key.getReferencedColumn())
            ) {
                sb.append("ALTER TABLE `").append(key.getTableName()).append("` DROP FOREIGN KEY `").append(key.getName()).append("`;");
            }
        }
        return sb.toString();
    }

    @JsonIgnore
    @Override
    public String getUpdateSql() {
        return null;
    }

    @JsonIgnore
    @Override
    public String getDeleteSql() {
        return "DROP TABLE `" + this.name + "`;";
    }

    @JsonIgnore
    @Override
    public String getCreateSql() {
        String sql = SQL;
        sql = sql.replace("${tableName}", this.name);
        sql = sql.replace("${engine}", " ENGINE = " + this.engine);
        if (this.getDatabase().getInfo().getIgnoreCharacterCompare()) {
            sql = sql.replace("${charset}", "");
            sql = sql.replace("${collate}", "");
        } else {
            sql = sql.replace("${charset}", " DEFAULT CHARSET = " + this.charset);
            sql = sql.replace("${collate}", " COLLATE = " + this.collate);
        }
        sql = sql.replace("${comment}", ObjectUtils.isEmpty(this.comment) ? "" : " COMMENT = '" + this.comment + "'");
        StringBuilder sb = new StringBuilder();
        for (Column column : this.columns) {
            String columnSql = column.getCreateTableSql();
            sb.append(columnSql);
        }
        List<Key> uniqueIndexList = this.keys.stream().filter(s -> "unique_index".equals(s.getName())).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(uniqueIndexList)) {
            sb.append(" CONSTRAINT unique_index UNIQUE (");
            for (Key key : uniqueIndexList) {
                sb.append("`").append(key.getColumnName()).append("`, ");
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append("),");
            this.keys.removeAll(uniqueIndexList);
        }
        // 主键
        List<Key> primaryKeyList = this.keys.stream().filter(k -> "PRIMARY".equals(k.getName())).collect(Collectors.toList());
        if (primaryKeyList.size() > 0) {
            sb.append("PRIMARY KEY (");
            primaryKeyList.forEach(k -> {
                sb.append("`").append(k.getColumnName()).append("`,");
            });
            sb.delete(sb.length() - 1, sb.length());
            sb.append("),");
        }
        this.keys.removeAll(primaryKeyList);
        for (Key key : this.keys) {
            String keySql = key.getCreateTableSql();
            sb.append(keySql);
        }
        String columnSql = sb.substring(0, sb.length() - 1);
        sql = sql.replace("${columnSql}", columnSql);
        return sql;
    }

    public static List<Table> configure(Connection connection, Database database) throws SQLException {
        List<Table> list1 = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/table.sql"),
                Table.class, database.getInfo().getName());
        List<Column> list2 = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/column.sql"),
                Column.class, database.getInfo().getName());
        List<Key> list3 = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/key.sql"),
                Key.class, database.getInfo().getName());
        return list1.stream().peek(o -> {
            o.setDatabase(database);
            o.setColumns(list2.stream().peek(s -> s.setTable(o)).filter(s -> o.getName().equals(s.getTableName())).collect(Collectors.toList()));
            o.setKeys(list3.stream().peek(s -> s.setTable(o)).filter(s -> o.getName().equals(s.getTableName())).collect(Collectors.toList()));
        }).collect(Collectors.toList());
    }

}
